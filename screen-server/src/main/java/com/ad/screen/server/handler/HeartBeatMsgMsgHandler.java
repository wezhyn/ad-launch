package com.ad.screen.server.handler;

import com.ad.screen.server.vo.req.HeartBeatMsg;
import com.ad.screen.server.vo.resp.ConfirmScreenResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
public class HeartBeatMsgMsgHandler extends BaseMsgHandler<HeartBeatMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatMsg msg) throws Exception {
        String imei = msg.getEquipmentName();
        ConfirmScreenResponse confirmScreenResponse=new ConfirmScreenResponse(imei);
        write(ctx, confirmScreenResponse);
        log.debug("发送目标设备编号为:{}的心跳确认帧", msg.getEquipmentName());
    }
}
