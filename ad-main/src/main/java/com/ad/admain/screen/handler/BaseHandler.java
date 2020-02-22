package com.ad.admain.screen.handler;

import com.ad.admain.screen.vo.IScreenFrameServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
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
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {
    //消息流水号
    private static final AttributeKey<Short> SERIAL_NUMBER = AttributeKey.newInstance("serialNumber");

    /**
     * 递增获取流水号
     * @return
     */
    public short getSerialNumber(Channel channel){
        Attribute<Short> flowIdAttr = channel.attr(SERIAL_NUMBER);
        Short flowId = flowIdAttr.get();
        if (flowId == null) {
            flowId = 0;
        } else {
            flowId++;
        }
        flowIdAttr.set(flowId);
        return flowId;
    }

    public void write(ChannelHandlerContext ctx, IScreenFrameServer msg) {
        ctx.writeAndFlush(msg).addListener(future -> {
            if (!future.isSuccess()) {
                log.error("发送失败", future.cause());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught",cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //此实例项目只设置了全部超时时间,可以通过state分别做处理,客户端发送心跳维持连接
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.warn("客户端{}读取超时,关闭连接", ctx.channel().remoteAddress());
                ctx.close();
            } else if (state == IdleState.WRITER_IDLE) {
                log.warn("客户端{}写入超时", ctx.channel().remoteAddress());
            }else if(state == IdleState.ALL_IDLE){
                log.warn("客户端{}读取写入超时", ctx.channel().remoteAddress());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
