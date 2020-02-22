package com.ad.admain.screen.handler;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.HeartBeatMsg;
import com.ad.admain.screen.vo.req.ScreenRequest;
import com.ad.admain.screen.vo.resp.ConfirmScreenResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
@Qualifier("heartBeatHandler")
@ChannelHandler.Sharable
public class HeartBeatMsgHandler extends BaseHandler<HeartBeatMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatMsg msg) throws Exception {
        log.debug(msg.toString());
        ConfirmScreenResponse confirmScreenResponse = new ConfirmScreenResponse(msg);
        write(ctx,confirmScreenResponse);
    }
}
