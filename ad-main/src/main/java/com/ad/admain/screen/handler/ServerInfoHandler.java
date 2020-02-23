package com.ad.admain.screen.handler;

import com.ad.admain.screen.entity.ServerInfo;
import com.ad.admain.screen.service.ServerInfoService;
import io.netty.channel.ChannelHandlerContext;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.channels.SocketChannel;

/**
 * @ClassName ServerInfoHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 2:39
 * @Version 1.0
 */
@Component
public class ServerInfoHandler extends BaseMsgHandler {

    @Autowired
    ServerInfoService serverInfoService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
