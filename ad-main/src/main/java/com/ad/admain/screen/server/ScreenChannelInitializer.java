package com.ad.admain.screen.server;

import com.ad.admain.cache.EquipmentCacheService;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.screen.IdChannelPool;
import com.ad.admain.screen.codec.ScreenProtocolOutEncoder;
import com.ad.admain.screen.entity.Task;
import com.ad.admain.screen.handler.*;
import com.ad.admain.screen.vo.resp.AdScreenResponse;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public static final AttributeKey<List<Task>> TASK_LIST=AttributeKey.valueOf("TASK_LIST");
    public static final AttributeKey<boolean[]> TASK_STATUS=AttributeKey.valueOf("TASK_STATUS");

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

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //将channel注册到id池中
        final Long longId=idChannelPool.registerChannel(ch);
        ch.pipeline().channel().attr(REGISTERED_ID).setIfAbsent(longId);

        ch.pipeline().addLast(new ScreenProtocolOutEncoder());
        ch.pipeline().addLast(new LineBasedFrameDecoder(60, true, true));
        ch.pipeline().addLast(screenProtocolCheckInboundHandler);
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 60));
        ch.pipeline().addLast(typeHandler);
        ch.pipeline().addLast(heartBeatMsgHandler);
        ch.pipeline().addLast(gpsMsgHandler);
        ch.pipeline().addLast(confirmMsgHandler);

        ch.eventLoop().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        Long id=ch.pipeline().channel().attr(REGISTERED_ID).get();
                        log.debug("开始检查池中id为:{}任务列表", id);
                        List<Task> tasks=ch.pipeline().channel().attr(TASK_LIST).get();
                        //若任务表内的数据不为空则发送数据
                        if (tasks.size()==0) {
                            log.debug("id为:{}的设备还没收到任务", id);
                        } else {
                            //总任务数目小于25，填充空白帧
                            if (tasks.size()<25){

                                int index = 25-tasks.size();
                                for (int i =tasks.size();i<25;i++){
                                    Task blankTask = Task.builder()
                                            .adOrderId(0)
                                            .entryId(i)
                                            .verticalView(false)
                                            .order(null)
                                            .build();
                                    tasks.add(blankTask);
                                }
                            }
                            for (int i=1; i <= tasks.size(); i++) {
                                Task task=tasks.get(i - 1);
                                AdOrder order=task.getOrder();
                                AdScreenResponse adScreenResponse=AdScreenResponse.builder()
                                        .entryId(task.getEntryId())
                                        .view(task.getView())
                                        .verticalView(task.getVerticalView())
                                        .repeatNum(task.getRepeatNum())
                                        .viewLength((byte) task.getView().getBytes().length)
                                        .build();
                                ch.pipeline().channel().write(adScreenResponse);
                            }
                        }
                    }
                }
                , 300, 300, TimeUnit.SECONDS
        );
    }
}
