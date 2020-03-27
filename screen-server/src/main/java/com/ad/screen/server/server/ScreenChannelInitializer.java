package com.ad.screen.server.server;


import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.codec.ScreenProtocolOutEncoder;
import com.ad.screen.server.entity.FixedTask;
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
    public static final AttributeKey<AtomicBoolean> FIRST_READ_CHANNEL=AttributeKey.valueOf("channel_first_read");

    public static final AttributeKey<ScheduledFuture<?>> SCHEDULED_SEND=AttributeKey.valueOf("SCHEDULED_SEND_EVENT");


    public static final AttributeKey<HashMap<Integer, Task>> TASK_MAP=AttributeKey.valueOf("TASK_MAP");
    /**
     * 设备attr的key,{@link ScreenChannelInitializer#channelRead} 处设置
     */
    public final static AttributeKey<AdEquipment> EQUIPMENT=AttributeKey.valueOf("EQUIPMENT");

    /**
     * 5分钟内调度频率
     */
    public static final Integer SCHEDULE_NUM=25;
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
    CompleteMsgHandler completeMsgHandler;
    @Autowired
    private CompensateHandler compensateHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final Channel chChannel=ch.pipeline().channel();
        chChannel.attr(FIRST_READ_CHANNEL).set(new AtomicBoolean(false));
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


        final ScheduledFuture<?> scheduledSend=ch.eventLoop().scheduleAtFixedRate(()->{
                    try {
                        if (chChannel.attr(SCHEDULED_SEND).get()==null) {
//                            客户端被关闭，定时任务取消
                            return;
                        }
                        AdEquipment equipment=chChannel.attr(EQUIPMENT).get();
                        HashMap<Integer, Task> received=chChannel.attr(TASK_MAP).get();
                        //若任务表内的数据不为空则发送数据
                        if (received==null || received.size()==0) {
                            log.debug("id为:{}的设备还没收到任务", equipment.getKey());
                        } else {
                            //遍历检查是否有新未发送的task,有则更新任务列表后空白帧的信息
                            for (Map.Entry<Integer, Task> entry : received.entrySet()) {
                                Task task=entry.getValue();
                                if (task.getPreTask()==null) {
                                    FixedTask fixedTask=new FixedTask(task);
                                    task.setPreTask(fixedTask);
                                    //先将未发送的数据放入缓冲区
                                    chChannel.write(createResponse(task, fixedTask.getRepeatNum(), equipment.getKey()));
                                }
                                //将消息推送到设备上
                                chChannel.flush();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                , 20, 30, TimeUnit.SECONDS
        );

        chChannel.attr(SCHEDULED_SEND).set(scheduledSend);
    }


    private AdScreenResponse createResponse(Task task, int repeatNum, String iemi) {
        return AdScreenResponse.builder()
                .entryId(task.getEntryId())
                .view(task.getView())
                .verticalView(task.getVerticalView())
                .repeatNum(repeatNum)
                .imei(iemi)
                .viewLength(task.getView()==null ? (byte) 0 : (byte) task.getView().getBytes().length)
                .build();
    }

}






