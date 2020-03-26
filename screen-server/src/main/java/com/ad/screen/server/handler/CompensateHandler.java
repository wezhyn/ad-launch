package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.AdRemoteOrder;
import com.ad.launch.order.RemoteAdOrderServiceI;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.screen.server.FailTaskService;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.mq.CommonSendCallback;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wezhyn
 * @since 03.25.2020
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class CompensateHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    IdChannelPool idChannelPool;
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Autowired
    FailTaskService failTaskService;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;
    @Resource
    private RemoteEquipmentServiceI equipmentService;
    @Resource
    private RemoteAdOrderServiceI adOrderService;

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
        } else {
//            远程主机强制关闭
            //持久化未完成任务
            if (!(evt instanceof ChannelInputShutdownReadComplete)) {
                exceptionCaught(ctx, new RuntimeException("未处理的异常"));
            }
            if (ctx.channel().isOpen()) {
//                避免多次
                compensate(ctx);
            }
        }
    }

    public void compensate(ChannelHandlerContext ctx) {
        final ScheduledFuture<?> future=ctx.channel().attr(ScreenChannelInitializer.SCHEDULED_SEND).get();
        if (future!=null) {
            future.cancel(false);
        }
        //需要在这里判断是否有未处理的任务   直接在ctx里存放两个hashmap 一个存放每个条目编号的对应在消息队列中的
        //id  一个用于存放随着任务完成帧提交时每一个人物的完成状态
        HashMap<Integer, Task> unFinishedTasks=ctx.channel().attr(ScreenChannelInitializer.TASK_MAP).get();
        if (unFinishedTasks!=null && unFinishedTasks.size()!=0) {
            HashMap<Integer, FailTask> hashMap=new HashMap<>();
            for (Map.Entry<Integer, Task> entry : unFinishedTasks.entrySet()) {
                Task task=entry.getValue();
                //整合一个FailTask集成小的task的重复执行任务次数
                FailTask failTask=hashMap.get(task.getOid());
                if (failTask==null) {
                    failTask=new FailTask();
                    TaskKey taskKey=new TaskKey(task.getOid(), task.getUid());
                    failTask.setId(taskKey);
                    failTask.setRate(1);
                    failTask.setView(task.getView());
                    failTask.setVerticalView(task.getVerticalView());
                    failTask.setRepeatNum(task.getRepeatNum());
                } else {
                    //如果改订单id已经在该hashmap中存在，则在该基础上增加未完成的执行次数,并增加1点频率
                    failTask.setRepeatNum(failTask.getRepeatNum() + task.getRepeatNum());
                    failTask.setRate(1 + failTask.getRate());
                }
                hashMap.put(task.getOid(), failTask);
            }
            //遍历失败任务的hashmap,并将其整合持久化到数据库
            for (Map.Entry<Integer, FailTask> entry :
                    hashMap.entrySet()) {
                Integer key=entry.getKey();
                FailTask tempTask=entry.getValue();
                FailTask failTask=failTaskService.findByKey(tempTask.getId());

                if (failTask==null) {
                    AdRemoteOrder adRemoteOrder=adOrderService.findByOid(tempTask.getId().getOid());

                    failTask=FailTask.builder()
                            .id(tempTask.getId())
                            .latitude(adRemoteOrder.getLatitude())
                            .longitude(adRemoteOrder.getLongitude())
                            .rate(adRemoteOrder.getRate())
                            .repeatNum(tempTask.getRepeatNum())
                            .scope(adRemoteOrder.getScope())
                            .view(tempTask.getView())
                            .verticalView(tempTask.isVerticalView())
                            .build();
                    failTaskService.save(failTask);
                } else {
                    failTask.setRepeatNum(tempTask.getRepeatNum() + failTask.getRepeatNum());
                    failTaskService.save(failTask);
                }
                rocketMQTemplate.asyncSend("fail_task_topic", failTask, new CommonSendCallback<>(failTask));
            }
        }

        //从id-channel池中删除掉这个channel
        idChannelPool.unregisterChannel(ctx.channel());
        //更新设备的在线状态
        AdEquipment equipment=ctx.channel().attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).get();
        equipment.setStatus(false);
        //保存设备的最终信息
        preserveEquipInfo(equipment);
        //在缓存中移除该信息
        pooledIdAndEquipCacheService.remove(equipment.getKey());
    }

    /**
     * @param adEquipment equip
     * @return void
     * @Description //保存超时时设备最后的状态信息
     * @Date 2020/3/13 20:58
     **/
    public void preserveEquipInfo(AdEquipment adEquipment) {
        equipmentService.mergeEquip(adEquipment);
    }

}
