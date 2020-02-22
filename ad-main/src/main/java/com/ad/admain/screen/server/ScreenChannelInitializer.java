package com.ad.admain.screen.server;

import com.ad.admain.screen.codec.ScreenProtocolInDecoder;
import com.ad.admain.screen.codec.ScreenProtocolOutEncoder;
import com.ad.admain.screen.handler.GpsMsgHandler;
import com.ad.admain.screen.handler.HeartBeatMsgHandler;
import com.ad.admain.screen.handler.TypeHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
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

    @Autowired
    GpsMsgHandler gpsMsgHandler;

    @Autowired
    TypeHandler typeHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LineBasedFrameDecoder(60, true, true));
        ch.pipeline().addLast(new ScreenProtocolOutEncoder());
        ch.pipeline().addLast(new IdleStateHandler(0,0,60));
        ch.pipeline().addLast(typeHandler);
        ch.pipeline().addLast(heartBeatMsgHandler);
        ch.pipeline().addLast(gpsMsgHandler);
    }
}
