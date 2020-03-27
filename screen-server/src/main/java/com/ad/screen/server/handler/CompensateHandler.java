package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.screen.server.ChannelFirstReadEvent;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.event.AllocateEvent;
import com.ad.screen.server.mq.DistributeTaskI;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.service.CompletionService;
import com.ad.screen.server.vo.resp.AdScreenResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ad.screen.server.server.ScreenChannelInitializer.EQUIPMENT;

/**
 * @author wezhyn
 * @since 03.25.2020
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class CompensateHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;
    @Resource
    private RemoteEquipmentServiceI equipmentService;

    @Autowired
    private DistributeTaskI distributeTask;

    @Autowired
    private CompletionService completionService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final ScheduledFuture<?> future=ctx.channel().attr(ScreenChannelInitializer.SCHEDULED_SEND).get();
        if (future!=null) {
            future.cancel(false);
        }
        super.channelInactive(ctx);
    }

    /**
     * 规定时间内未收到心跳帧则断开连接并将该设备的状态设置为未在线的状态
     * 避免主机强制退出后，循环 BaseHandler 触发补偿
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //项目只设置了全部超时时间
            IdleState state=((IdleStateEvent) evt).state();
            //关闭连接: 长时间未接收心跳帧，或读写均为执行
            if (state==IdleState.READER_IDLE || state==IdleState.ALL_IDLE) {
                compensate(ctx);
                log.warn("客户端{}读取写入超时", ctx.channel().remoteAddress());
            }
        } else if (evt instanceof ChannelFirstReadEvent) {
//          填充25个空白帧
            final AtomicBoolean firstRead=ctx.channel().attr(ScreenChannelInitializer.FIRST_READ_CHANNEL).get();
            if (firstRead.compareAndSet(false, true)) {
                final Channel channel=ctx.channel();
                for (int i=1; i <= ScreenChannelInitializer.SCHEDULE_NUM; i++) {
                    final AdEquipment equipment=ctx.channel().attr(EQUIPMENT).get();
                    AdScreenResponse blankResponse=AdScreenResponse.builder()
                            .entryId(i)
                            .view("")
                            .verticalView(false)
                            .repeatNum(9999)
                            .imei(equipment.getKey())
                            .viewLength((byte) 0)
                            .build();
                    channel.write(blankResponse);
                }
                channel.flush();
            }
        } else {
//            远程主机强制关闭
            //持久化未完成任务
            if (!(evt instanceof ChannelInputShutdownReadComplete)) {
                exceptionCaught(ctx, new RuntimeException("未处理的异常"));
            }
            compensate(ctx);
        }
    }

    public void compensate(ChannelHandlerContext ctx) {
        //id  一个用于存放随着任务完成帧提交时每一个人物的完成状态
        HashMap<Integer, Task> unFinishedTasks=ctx.channel().attr(ScreenChannelInitializer.TASK_MAP).get();
        if (unFinishedTasks==null || unFinishedTasks.size()==0) {
            return;
        }
//        order,equipTask
        HashMap<TaskKey, EquipTask.EquipTaskBuilder> constructEquipTask=new HashMap<>(8);
        for (Task task : unFinishedTasks.values()) {
            if (!constructEquipTask.containsKey(task.getTaskKey())) {
                constructEquipTask.putIfAbsent(task.getTaskKey(), fromTask(task));
            } else {
//              先转移当前类帧的数据从内存到数据库（防止设备在熄火前发送了完成帧，而系统还未处理）
                completionService.memoryToDisk(task.getOrderId(), task.getDeliverUserId());
                final EquipTask.EquipTaskBuilder equipTask=constructEquipTask.get(task.getTaskKey());
                equipTask.totalNumInc(getTaskNum(task))
                        .rateInc(1);
            }
        }
//        当前服务器转移任务, 自此，旧数据已经被持久化
        for (EquipTask.EquipTaskBuilder builder : constructEquipTask.values()) {
            taskExecutor.submit(new TransferRunner(builder.build(), distributeTask, taskExecutor, applicationEventPublisher));
        }
        //更新设备的在线状态
        AdEquipment channelEquip=ctx.channel().attr(EQUIPMENT).get();
        taskExecutor.submit(()->{
            channelEquip.setStatus(false);
            //保存设备的最终信息
            preserveEquipInfo(channelEquip);
        });
        //在缓存中移除该信息
        pooledIdAndEquipCacheService.remove(channelEquip.getKey());
    }

    /**
     * 保存超时时设备最后的状态信息
     *
     * @param adEquipment equip
     * @return void
     **/
    public void preserveEquipInfo(AdEquipment adEquipment) {
        equipmentService.mergeEquip(adEquipment);
    }

    private int getTaskNum(Task task) {
        int num=task.getRepeatNum();
        if (task.getPreTask()!=null) {
//            当前发布的任务还未被响应，设备已经熄火
            num+=task.getPreTask().getRepeatNum();
        } else {
//            当前已经被处理，且还未开始调度
        }
        return num;
    }

    private EquipTask.EquipTaskBuilder fromTask(Task task) {
        return EquipTask.EquipTaskBuilder.anEquipTask()
                .vertical(task.getVerticalView())
                .totalNum(getTaskNum(task))
                .taskKey(task.getTaskKey())
                .screenView(task.getView())
                .scope(task.getScope())
                .rate(task.getRate())
                .longitude(task.getLongitude())
                .latitude(task.getLatitude())
                .deliverNum(task.getDeliverNum())
                .executedNum(0);

    }

    private static class TransferRunner implements Runnable {
        /**
         * 当重复太多次后，丢弃当前任务，由重启线程恢复当前订单
         */
        public static final Integer MAX_RETRY=5;
        private EquipTask task;
        private DistributeTaskI distributeTask;
        private ThreadPoolTaskExecutor threadPoolTaskExecutor;
        private ApplicationEventPublisher applicationEventPublisher;
        private AtomicInteger retry;


        public TransferRunner(EquipTask task, DistributeTaskI distributeTask, ThreadPoolTaskExecutor threadPoolTaskExecutor, ApplicationEventPublisher applicationEventPublisher) {
            this.task=task;
            this.distributeTask=distributeTask;
            this.threadPoolTaskExecutor=threadPoolTaskExecutor;
            this.applicationEventPublisher=applicationEventPublisher;
            retry=new AtomicInteger(0);
        }

        @Override
        public void run() {
            final Optional<PooledIdAndEquipCache> availableSingleEquip=distributeTask.availableSingleEquip(task);
            if (!availableSingleEquip.isPresent()) {
                if (this.retry.getAndIncrement() > MAX_RETRY) {
//                  丢弃当前任务
                    return;
                } else {
                    threadPoolTaskExecutor.submit(this);
                    return;
                }
            }
            final PooledIdAndEquipCache equipChannel=availableSingleEquip.get();
            applicationEventPublisher.publishEvent(new AllocateEvent(this, true, task, Collections.singletonList(equipChannel)));
        }
    }
}
