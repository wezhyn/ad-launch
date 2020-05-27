package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.screen.server.ChannelFirstReadEvent;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.event.DistributeTaskI;
import com.ad.screen.server.event.LocalResumeServerListener;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.service.DistributeTaskService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ad.screen.server.server.ScreenChannelInitializer.FIRST_READ_CHANNEL;


/**
 * @author wezhyn
 * @since 03.25.2020
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class CompensateHandler extends ChannelInboundHandlerAdapter {


    final
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;
    @Resource
    private RemoteEquipmentServiceI equipmentService;
    private final DistributeTaskI distributeTask;
    private final ScheduledExecutorService taskExecutor;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LocalResumeServerListener resumeServerListener;
    private final DistributeTaskService distributeTaskService;


    public CompensateHandler(PooledIdAndEquipCacheService pooledIdAndEquipCacheService, DistributeTaskI distributeTask,
                             @Qualifier("self_taskExecutor") ScheduledExecutorService taskExecutor,
                             ApplicationEventPublisher applicationEventPublisher,
                             LocalResumeServerListener resumeServerListener, DistributeTaskService distributeTaskService) {
        this.pooledIdAndEquipCacheService = pooledIdAndEquipCacheService;
        this.distributeTask = distributeTask;
        this.taskExecutor = taskExecutor;
        this.applicationEventPublisher = applicationEventPublisher;
        this.resumeServerListener = resumeServerListener;
        this.distributeTaskService = distributeTaskService;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        final ScheduledFuture<?> future = ctx.channel().attr(ScreenChannelInitializer.SCHEDULED_SEND).get();
        if (future != null) {
            future.cancel(false);
        }
        compensate(ctx);
    }

    /**
     * 规定时间内未收到心跳帧则断开连接并将该设备的状态设置为未在线的状态
     * 避免主机强制退出后，循环 BaseHandler 触发补偿
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof ChannelFirstReadEvent) {
//          填充25个空白帧
            final AtomicBoolean firstRead = ctx.channel().attr(FIRST_READ_CHANNEL).get();
            if (firstRead.compareAndSet(false, true)) {
                final String iemi = ctx.channel().attr(ScreenChannelInitializer.IEMI).get();
                log.info("加载 ：{}", iemi);
            }
        } else {
//            远程主机强制关闭
            //持久化未完成任务
            if (!(evt instanceof ChannelInputShutdownReadComplete)) {
                throw new RuntimeException("未处理的异常: " + evt.getClass());
            }
            log.error("event: {}", evt);
            compensate(ctx);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof ReadTimeoutException) {
            log.warn("客户端{}读取写入超时", ctx.channel().remoteAddress());
        }
        log.error("error:", cause);
    }

    public void compensate(ChannelHandlerContext ctx) {
        final AtomicBoolean channelInitCompleted = ctx.channel().attr(FIRST_READ_CHANNEL).get();
        final PooledIdAndEquipCache equipCache = ctx.channel().attr(ScreenChannelInitializer.POOLED_EQUIP_CACHE).get();
        if (channelInitCompleted == null || !channelInitCompleted.get() || equipCache == null ||
                equipCache.isChannelClose()) {
//            还未完成初始化，客户端就被关闭
            return;
        } else {
            log.warn("{} 关闭", equipCache.getEquipment().getKey());
            equipCache.setChannelClose(true);
        }
        try {
            Map<Integer, Task> unFinishedTasks = equipCache.getAllTask();
            if (unFinishedTasks == null || unFinishedTasks.size() == 0) {
                return;
            }
            HashMap<TaskKey, EquipTask.EquipTaskBuilder> constructEquipTask = new HashMap<>(8);
            for (Task task : unFinishedTasks.values()) {
                if (!constructEquipTask.containsKey(task.getTaskKey())) {
                    constructEquipTask.putIfAbsent(task.getTaskKey(), fromTask(task));
                } else {
//                异常调度导致同 EquipTask 里的不同车到一辆车上
                    constructEquipTask.get(task.getTaskKey())
//                        添加剩余数量
                            .totalNumInc(getTaskNum(task))
                            .driverNumInc(1);
                }
            }
            for (EquipTask.EquipTaskBuilder builder : constructEquipTask.values()) {
                final EquipTask task = builder.build();
                taskExecutor.execute(new TransferTask(task, distributeTask,
                        applicationEventPublisher));
                log.debug("提交任务转移 : orderId {}", task.getTaskKey().getOid());
                distributeTaskService.remove(task.getTaskKey());
            }
            //更新设备的在线状态
            AdEquipment channelEquip = equipCache.getEquipment();
            taskExecutor.submit(() -> {
                channelEquip.setStatus(false);
                //保存设备的最终信息
                preserveEquipInfo(channelEquip);
            });
        } finally {
            //在缓存中移除该信息
            pooledIdAndEquipCacheService.remove(equipCache.getEquipment().getKey());
        }
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
        int num = task.getRepeatNum();
        if (task.getPreTask() != null) {
//            当前发布的任务还未被响应，设备已经熄火
            num += task.getPreTask().getRepeatNum();
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


}
