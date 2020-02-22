package com.ad.admain.screen.server;

import com.ad.admain.screen.codec.ScreenProtocolOutEncoder;
import com.ad.admain.screen.handler.GpsMsgHandler;
import com.ad.admain.screen.handler.HeartBeatMsgHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
public class ScreenChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    @Value("${netty.server.allTimeout}")
    private int allTimeOut;

    @Autowired
    HeartBeatMsgHandler heartBeatMsgHandler;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final ByteBuf delimiter= Unpooled.copiedBuffer("SOF".getBytes());
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new IdleStateHandler(0,0,allTimeOut, TimeUnit.SECONDS));
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(55, false, delimiter));
        ch.pipeline().addLast(new ScreenProtocolOutEncoder());
        ch.pipeline().addLast(new HeartBeatMsgHandler());
        ch.pipeline().addLast(new GpsMsgHandler());
        ch.pipeline().addLast(new IdleStateHandler(0,0,60));


    }
}
