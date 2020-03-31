package com.ad.screen.client;

import com.ad.screen.client.vo.req.ConfirmMsg;
import com.ad.screen.client.vo.req.HeartBeatMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class ConfirmHandler extends SimpleChannelInboundHandler<ConfirmMsg> {
    private Random r=new Random();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ConfirmMsg confirmMsg) throws Exception {
//        log.info("接收到确认帧：{}", confirmMsg);
/*        if (r.nextInt(20)==0) {
            channelHandlerContext.channel().close().sync();
        }*/
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e=(IdleStateEvent) evt;
            if (e.state()==IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state()==IdleState.WRITER_IDLE) {
                String equip=ctx.channel().attr(ScreenClient.REGISTERED_ID).get();
                ctx.writeAndFlush(new HeartBeatMsg(equip));
            } else if (e.state()==IdleState.ALL_IDLE) {
                ctx.close();
            }
        }
    }
}
