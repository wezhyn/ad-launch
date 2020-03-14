package com.ad.screen.server.server;


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
                        try {
                            Long id=ch.pipeline().channel().attr(REGISTERED_ID).get();
                            log.debug("开始检查池中id为:{}任务列表", id);
                            Channel channel = idChannelPool.getChannel(id);
                            List<Task> tasks = channel.attr(TASK_LIST).get();
                            //若任务表内的数据不为空则发送数据
                            if (tasks==null||tasks.size()==0) {
                                log.debug("id为:{}的设备还没收到任务", id);
                            } else {
                                //总任务数目小于25，填充空白帧
                                if (tasks.size() < 25) {
                                    int index=25 - tasks.size();
                                    for (int i=tasks.size(); i < 25; i++) {
                                        Task blankTask=Task.builder()
                                                .adOrderId(0)
                                                .entryId(i)
                                                .repeatNum(Integer.MAX_VALUE)
                                                .verticalView(false)
                                                .build();
                                        tasks.add(blankTask);
                                    }
                                }
                                for (int i=0; i <tasks.size(); i++) {
                                    Task task=tasks.get(i);
                                    AdScreenResponse adScreenResponse=AdScreenResponse.builder()
                                            .entryId(task.getEntryId())
                                            .view(task.getView())
                                            .verticalView(task.getVerticalView())
                                            .repeatNum(task.getRepeatNum())
                                            .view("比划比划")
                                            .viewLength(task.getView()==null?(byte) 0 :(byte)task.getView().getBytes().length)
                                            .build();
                                    channel.writeAndFlush(adScreenResponse);
                                    log.info("发送第{}条广告",i+1);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , 20, 20, TimeUnit.SECONDS
        );
    }
}
