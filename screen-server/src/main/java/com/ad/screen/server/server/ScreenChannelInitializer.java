package com.ad.screen.server.server;


import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.EquipmentCacheService;
import com.ad.screen.server.codec.ScreenProtocolOutEncoder;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.handler.*;
import com.ad.screen.server.vo.resp.AdScreenResponse;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.ad.screen.server.handler.ScreenProtocolCheckInboundHandler.EQUIPMENT;

/**
 * @ClassName ChannelInitializer
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 1:10
 * @Version 1.0
 */
@Component
@Slf4j
public class ScreenChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {
    public static final AttributeKey<Long> REGISTERED_ID=AttributeKey.valueOf("REGISTERED_ID");
    public static final AttributeKey<ScheduledFuture<?>> SCHEDULED_SEND=AttributeKey.valueOf("SCHEDULED_SEND_EVENT");
    public static final AttributeKey<HashMap<Integer, Task>> TASK_MAP=AttributeKey.valueOf("TASK_MAP");
//    public static final AttributeKey<boolean[]> TASK_STATUS = AttributeKey.valueOf("TASK_STATUS");

    @Value("${netty.server.allTimeout}")
    private int allTimeOut;

    @Autowired
    HeartBeatMsgMsgHandler heartBeatMsgHandler;

    @Autowired
    GpsMsgMsgHandler gpsMsgHandler;

    @Autowired
    TypeMsgHandler typeHandler;

    @Autowired
    ConfirmMsgHandler confirmMsgHandler;

    @Autowired
    ScreenProtocolCheckInboundHandler screenProtocolCheckInboundHandler;

    @Autowired
    private IdChannelPool idChannelPool;

    @Autowired
    EquipmentCacheService equipmentCache;
    @Autowired
    CompleteMsgHandler completeMsgHandler;
    @Autowired
    private CompensateHandler compensateHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //将channel注册到id池中
        final Long longId=idChannelPool.registerChannel(ch);
        final Channel chChannel=ch.pipeline().channel();
        chChannel.attr(REGISTERED_ID).setIfAbsent(longId);
        ch.pipeline().addLast(new ScreenProtocolOutEncoder());
        ch.pipeline().addLast(new LineBasedFrameDecoder(60, true, true));
        ch.pipeline().addLast(screenProtocolCheckInboundHandler);
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 60));
        ch.pipeline().addLast(typeHandler);
        ch.pipeline().addLast(heartBeatMsgHandler);
        ch.pipeline().addLast(gpsMsgHandler);
        ch.pipeline().addLast(confirmMsgHandler);
        ch.pipeline().addLast(completeMsgHandler);
        ch.pipeline().addLast(compensateHandler);

//dev
//         ch.eventLoop().schedule(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Long id=ch.pipeline().channel().attr(REGISTERED_ID).get();
//                            AdEquipment equipment = ch.pipeline().channel().attr(EQUIPMENT).get();
//                            log.debug("开始检查池中id为:{}任务列表", id);
//                            Channel channel = idChannelPool.getChannel(id);
//                            List<Task> tasks = channel.attr(TASK_LIST).get();
//                            //若任务表内的数据不为空则发送数据
//                            if (tasks==null||tasks.size()==0) {
//                                log.debug("id为:{}的设备还没收到任务", id);
//                            } else {
//                                //总任务数目小于25，填充空白帧
//                                if (tasks.size() < 25) {
//                                    int index=25 - tasks.size();
//                                    for (int i=tasks.size(); i < 25; i++) {
//                                        Task blankTask=Task.builder()
//                                                .oid(0)
//                                                .entryId(i+1)
//                                                .view("")
//                                                .repeatNum(Integer.MAX_VALUE)
//                                                .verticalView(false)
//                                                .build();
//                                        tasks.add(blankTask);
//                                    }
//                                }
//
//                                for (int i=0; i <tasks.size(); i++) {
//                                    Task task=tasks.get(i);
//                                    AdScreenResponse adScreenResponse=AdScreenResponse.builder()
//                                            .entryId(task.getEntryId())
//                                            .view(task.getView())
//                                            .verticalView(task.getVerticalView())
//                                            .repeatNum(task.getRepeatNum())
//                                            .imei(equipment.getKey())
//                                            .viewLength(task.getView()==null?(byte) 0 :(byte)task.getView().getBytes().length)
//                                            .build();
//                                    channel.write(adScreenResponse);
//                                }
//                                channel.flush();
//                                log.info("发送25条广告到设备上");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }                    }
//                }, 30, TimeUnit.SECONDS);

//pro
        final ScheduledFuture<?> scheduledSend=ch.eventLoop().scheduleAtFixedRate(()->{
                    try {
                        if (chChannel.attr(SCHEDULED_SEND).get()==null) {
//                            客户端被关闭，定时任务取消
                            return;
                        }
                        Long id=chChannel.attr(REGISTERED_ID).get();
                        AdEquipment equipment=chChannel.attr(EQUIPMENT).get();
                        log.debug("开始检查池中id为:{}任务列表", id);
                        Channel channel=idChannelPool.getChannel(id);
                        HashMap<Integer, Task> received=channel.attr(TASK_MAP).get();
                        //若任务表内的数据不为空则发送数据
                        if (received==null || received.size()==0) {
                            log.debug("id为:{}的设备还没收到任务", id);
                        } else {
                            //遍历检查是否有新未发送的task,有则更新任务列表后空白帧的信息
                            for (Map.Entry<Integer, Task> entry :
                                    received.entrySet()) {
                                Task task=entry.getValue();
                                if (!task.getSendIf()) {
                                    AdScreenResponse adScreenResponse=AdScreenResponse.builder()
                                            .entryId(task.getEntryId())
                                            .view(task.getView())
                                            .verticalView(task.getVerticalView())
                                            .repeatNum(task.getRepeatNum())
                                            .imei(equipment.getKey())
                                            .viewLength(task.getView()==null ? (byte) 0 : (byte) task.getView().getBytes().length)
                                            .build();
                                    //先将未发送的数据放入缓冲区
                                    channel.write(adScreenResponse);
                                    task.setSendIf(true);
//                                        newReceived.add(task);
                                    received.put(task.getEntryId(), task);
                                } else {
//                                        newReceived.add(task);
                                    continue;
                                }

                            }
                            //补充空白帧并将其写入缓冲区(该操作可以满足有新任务未发送时的情况)
//                                for (int i = received.size(); i < 25; i++) {
//                                    AdScreenResponse blankResponse = AdScreenResponse.builder()
//                                            .entryId(i)
//                                            .view("")
//                                            .verticalView(false)
//                                            .repeatNum(9999)
//                                            .imei(equipment.getKey())
//                                            .viewLength((byte) 0)
//                                            .build();
//                                    channel.write(blankResponse);
//                                }
                            //获取keySet在如果keySet不包含i则用空白帧填充，以维持设备的频率
                            Set<Integer> keys=received.keySet();
                            for (int i=1; i <= 25; i++) {
                                if (!keys.contains(i)) {
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
                            }
                            //将消息推送到设备上
                            channel.flush();
                            channel.attr(TASK_MAP).set(received);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                , 20, 30, TimeUnit.SECONDS
        );

        chChannel.attr(SCHEDULED_SEND).set(scheduledSend);
    }
}






