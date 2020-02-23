package com.ad.admain.screen.server;

import com.ad.admain.screen.codec.ScreenProtocolOutEncoder;
import com.ad.admain.screen.handler.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    HeartBeatMsgMsgHandler heartBeatMsgHandler;

    @Autowired
    GpsMsgMsgHandler gpsMsgHandler;

    @Autowired
    TypeMsgHandler typeHandler;

    @Autowired
    ConfirmMsgHandler confirmMsgHandler;

    @Autowired
    ScreenProtocolCheckInboundHandler screenProtocolCheckInboundHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ScreenProtocolOutEncoder());
        ch.pipeline().addLast(new LineBasedFrameDecoder(60, true, true));
        ch.pipeline().addLast(screenProtocolCheckInboundHandler);
        ch.pipeline().addLast(new IdleStateHandler(0,0,60));
        ch.pipeline().addLast(typeHandler);
        ch.pipeline().addLast(heartBeatMsgHandler);
        ch.pipeline().addLast(gpsMsgHandler);
        ch.pipeline().addLast(confirmMsgHandler);
    }
}
