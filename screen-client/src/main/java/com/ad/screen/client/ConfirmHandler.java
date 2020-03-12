package com.ad.screen.client;

import com.ad.screen.client.vo.req.ConfirmMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class ConfirmHandler extends SimpleChannelInboundHandler<ConfirmMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ConfirmMsg confirmMsg) throws Exception {
        log.info("接收到确认帧：{}", confirmMsg);
    }
}
