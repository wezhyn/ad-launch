package com.ad.admain.screen.handler;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.ScreenRequest;
import com.ad.admain.screen.vo.resp.ConfirmScreenResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @ClassName HeartBeatHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/20 14:15
 * @Version 1.0
 */
@Component
@Qualifier("heartBeatHandler")
@ChannelHandler.Sharable
public class HeartBeatHandler extends SimpleChannelInboundHandler<ScreenRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ScreenRequest msg) throws Exception {
        FrameType type = msg.getFrameType();
        if (type==FrameType.HEART_BEAT){
            sendPongMsg(ctx,msg);
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //未收到客户端的心跳帧，则断开连接
        if (evt instanceof IdleStateEvent){
            IdleStateEvent evnet = (IdleStateEvent) evt;
            IdleState idleState = evnet.state();
            if (idleState ==IdleState.ALL_IDLE){
                ctx.disconnect();
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    public void sendPongMsg(ChannelHandlerContext channelHandlerContext, ScreenRequest msg){
        ConfirmScreenResponse pong = new ConfirmScreenResponse(msg);
        channelHandlerContext.writeAndFlush(pong);

    }
}
