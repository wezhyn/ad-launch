package com.ad.screen.server.server;


import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.entity.FixedTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.handler.*;
import com.ad.screen.server.vo.resp.AdScreenResponse;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public static final AttributeKey<AtomicBoolean> FIRST_READ_CHANNEL = AttributeKey.valueOf("channel_first_read");

    public static final AttributeKey<ScheduledFuture<?>> SCHEDULED_SEND = AttributeKey.valueOf("SCHEDULED_SEND_EVENT");
    /**
     * 设备attr的key,{@link ScreenChannelInitializer#channelRead} 处设置
     */
    public static final AttributeKey<String> IEMI = AttributeKey.valueOf("EQUIP_IEMI");
    public static final AttributeKey<PooledIdAndEquipCache> POOLED_EQUIP_CACHE = AttributeKey.valueOf("EQUIP_POOLED_CACHE");
    /**
     * 5分钟内调度频率
     */
    public static final Integer SCHEDULE_NUM = 25;
    private final HeartBeatMsgMsgHandler heartBeatMsgHandler;
    private final GpsMsgMsgHandler gpsMsgHandler;
    private final ConfirmMsgHandler confirmMsgHandler;
    private final ScreenProtocolCheckInboundHandler screenProtocolCheckInboundHandler;
    private final CompleteMsgHandler completeMsgHandler;
    private final CompensateHandler compensateHandler;

    public ScreenChannelInitializer(HeartBeatMsgMsgHandler heartBeatMsgHandler, GpsMsgMsgHandler gpsMsgHandler, ConfirmMsgHandler confirmMsgHandler, ScreenProtocolCheckInboundHandler screenProtocolCheckInboundHandler, CompleteMsgHandler completeMsgHandler, CompensateHandler compensateHandler) {
        this.heartBeatMsgHandler = heartBeatMsgHandler;
        this.gpsMsgHandler = gpsMsgHandler;
        this.confirmMsgHandler = confirmMsgHandler;
        this.screenProtocolCheckInboundHandler = screenProtocolCheckInboundHandler;
        this.completeMsgHandler = completeMsgHandler;
        this.compensateHandler = compensateHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final Channel chChannel = ch.pipeline().channel();
        chChannel.attr(FIRST_READ_CHANNEL).set(new AtomicBoolean(false));
//        ch.pipeline().addLast(new ReadTimeoutHandler(130));
        ch.pipeline().addLast(new ScreenProtocolOutEncoder());
        ch.pipeline().addLast(new LineBasedFrameDecoder(80, true, true));
        ch.pipeline().addLast(screenProtocolCheckInboundHandler);
        ch.pipeline().addLast(heartBeatMsgHandler);
        ch.pipeline().addLast(gpsMsgHandler);
        ch.pipeline().addLast(confirmMsgHandler);
        ch.pipeline().addLast(completeMsgHandler);
        ch.pipeline().addLast(compensateHandler);


        final ScheduledFuture<?> scheduledSend = ch.eventLoop().scheduleAtFixedRate(() -> {
                    try {
                        final AtomicBoolean channelInitCompleted = chChannel.attr(FIRST_READ_CHANNEL).get();
                        if (chChannel.attr(SCHEDULED_SEND).get() == null || channelInitCompleted == null ||
                                !channelInitCompleted.get()) {
//                            客户端被关闭| 客户端还未被正常初始化
                            return;
                        }
                        final PooledIdAndEquipCache equipCache = chChannel.attr(POOLED_EQUIP_CACHE).get();
                        AdEquipment equipment = equipCache.getEquipment();
                        //若任务表内的数据不为空则发送数据
                        Map<Integer, Task> received = equipCache.getAllTask();
                        if (received.size() == 0) {
                            log.debug("id为:{}的设备还没收到任务", equipment.getKey());
                            return;
                        }
                        //遍历检查是否有新未发送的task,有则更新任务列表后空白帧的信息
                        for (Map.Entry<Integer, Task> entry : received.entrySet()) {
                            Task task = entry.getValue();

                            final FixedTask preTask = task.getPreTask();
                            if (preTask == null) {
                                FixedTask fixedTask = new FixedTask(task);
                                task.setPreTask(fixedTask);
                                //将消息推送到设备上
                                chChannel.writeAndFlush(createResponse(fixedTask, equipment.getKey()));
                            } else {
                                if (preTask.isSendAgain()) {
                                    chChannel.writeAndFlush(createResponse(preTask, equipment.getKey()));
                                    log.debug("重新发送编号为: {} 的数据帧", preTask.getEquipEntryId());
                                    preTask.resetRetry();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                , 0, 50, TimeUnit.SECONDS
        );

        chChannel.attr(SCHEDULED_SEND).set(scheduledSend);
    }


    private AdScreenResponse createResponse(FixedTask task, String iemi) {
        return AdScreenResponse.builder()
                .entryId(task.getEquipEntryId())
                .view(task.getTask().getView())
                .verticalView(task.getTask().getVerticalView() == null ? false : task.getTask().getVerticalView())
                .repeatNum(task.getRepeatNum())
                .imei(iemi)
                .viewLength((byte) task.getTask().getView().length())
                .build();
    }

}






