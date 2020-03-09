package com.ad.admain.screen.handler;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.req.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @ClassName TypeHandler
 * @Description 基础消息处理类
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 19:23
 * @Version 1.0
 */
@Component
@ChannelHandler.Sharable
public class TypeMsgHandler extends BaseMsgHandler<BaseScreenRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseScreenRequest msg) throws Exception {
        FrameType type=msg.getFrameType();
        switch (type) {
            case HEART_BEAT: {
                HeartBeatMsg heartBeatMsg=new HeartBeatMsg();
                BeanUtils.copyProperties(msg, heartBeatMsg);
                ctx.fireChannelRead(heartBeatMsg);
                break;
            }
            case GPS: {
                GpsMsg gpsMsg=new GpsMsg();
                BeanUtils.copyProperties(msg,gpsMsg);
                ctx.fireChannelRead(gpsMsg);
                break;
            }
            case CONFIRM:{
                ConfirmMsg confirmMsg = new ConfirmMsg();
                BeanUtils.copyProperties(msg,confirmMsg);
                ctx.fireChannelRead(confirmMsg);
                break;
            }
            case COMPLETE_NOTIFICATION:{
                CompleteNotificationMsg completeNotificationMsg = new CompleteNotificationMsg();
                BeanUtils.copyProperties(msg,completeNotificationMsg);
                ctx.fireChannelRead(completeNotificationMsg);
                break;
            }
        }
    }
}
