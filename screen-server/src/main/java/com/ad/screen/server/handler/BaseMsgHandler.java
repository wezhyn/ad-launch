package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.screen.server.FailTaskService;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.vo.IScreenFrameServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BaseHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 1:51
 * @Version 1.0
 */
@Slf4j

public abstract class BaseMsgHandler<T> extends SimpleChannelInboundHandler<T> {
    //消息流水号
    private static final AttributeKey<Short> SERIAL_NUMBER=AttributeKey.newInstance("serialNumber");
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

    /**
     * 递增获取流水号
     *
     * @return
     */
    public short getSerialNumber(Channel channel) {
        Attribute<Short> flowIdAttr=channel.attr(SERIAL_NUMBER);
        Short flowId=flowIdAttr.get();
        if (flowId==null) {
            flowId=0;
        } else {
            flowId++;
        }
        flowIdAttr.set(flowId);
        return flowId;
    }

    public void write(ChannelHandlerContext ctx, IScreenFrameServer msg) {
        ctx.writeAndFlush(msg).addListener(future->{
            if (!future.isSuccess()) {
                log.error("发送失败", future.cause());
            }
        });
    }

    @Override
    //规定时间内未收到心跳帧则断开连接并将该设备的状态设置为未在线的状态
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //持久化未完成任务
        compensate(ctx);
        if (evt instanceof IdleStateEvent) {
            //项目只设置了全部超时时间
            IdleState state=((IdleStateEvent) evt).state();
            //关闭连接
            if (state==IdleState.ALL_IDLE) {
                log.warn("客户端{}读取写入超时", ctx.channel().remoteAddress());
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }
    /**
     * @Description //保存超时时设备最后的状态信息
     * @Date 2020/3/13 20:58
     * @param adEquipment
     *@return void
     **/
    public void preserveEquipInfo(AdEquipment adEquipment){
        equipmentService.mergeEquip(adEquipment);
    }

    public void compensate(ChannelHandlerContext ctx){
        //需要在这里判断是否有未处理的任务   直接在ctx里存放两个hashmap 一个存放每个条目编号的对应在消息队列中的
        //id  一个用于存放随着任务完成帧提交时每一个人物的完成状态
        List<Task> unFinishedTasks = ctx.channel().attr(ScreenChannelInitializer.TASK_LIST).get();
        if (unFinishedTasks!=null && unFinishedTasks.size() != 0){
            HashMap<Integer, FailTask> hashMap=new HashMap<>();
            for (Task task : unFinishedTasks) {
                Integer id=task.getOid();
                //整合一个FailTask集成小的task的重复执行任务次数
                FailTask failTask=hashMap.get(id);
                if (failTask==null) {
                    failTask = new FailTask();
                    TaskKey taskKey = new TaskKey(task.getOid(),task.getUid());
                    failTask.setId(taskKey);
                    failTask.setRepeatNum(task.getRepeatNum());
                } else {
                    //如果改订单id已经在该hashmap中存在，则在该基础上增加未完成的执行次数
                    failTask.setRepeatNum(failTask.getRepeatNum() + task.getRepeatNum());
                }
                hashMap.put(id, failTask);
            }
            //遍历失败任务的hashmap,并将其整合持久化到数据库
            for (Map.Entry<Integer, FailTask> entry :
                    hashMap.entrySet()) {
                Integer key = entry.getKey();
                FailTask tempTask =  entry.getValue();
                FailTask failTask=failTaskService.findByKey(tempTask.getId());
                if (failTask==null) {
                    failTaskService.save(entry.getValue());
                } else {
                    failTask.setRepeatNum(tempTask.getRepeatNum() + failTask.getRepeatNum());
                    failTaskService.save(failTask);
                }
            }
        }

        //从id-channel池中删除掉这个channel
        idChannelPool.unregisterChannel(ctx.channel());
        //更新设备的在线状态
        AdEquipment equipment= ctx.channel().attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).get();
        equipment.setStatus(false);
        //保存设备的最终信息
        preserveEquipInfo(equipment);
        //在缓存中移除该信息
        pooledIdAndEquipCacheService.remove(equipment.getKey());
    }
}
