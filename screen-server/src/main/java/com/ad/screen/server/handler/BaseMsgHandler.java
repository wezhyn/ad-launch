package com.ad.screen.server.handler;

import com.ad.screen.server.vo.IScreenFrameServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName BaseHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 1:51
 * @Version 1.0
 */
@Slf4j

public abstract class BaseMsgHandler<T> extends SimpleChannelInboundHandler<T> {



    public void write(ChannelHandlerContext ctx, IScreenFrameServer msg) {
        ctx.writeAndFlush(msg).addListener(future->{
            if (!future.isSuccess()) {
                log.error("发送失败", future.cause());
            }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }


}
