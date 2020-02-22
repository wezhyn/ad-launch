package com.ad.admain.screen.handler;

import com.ad.admain.screen.vo.req.ConfirmMsg;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName ConfirmMsgHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 0:52
 * @Version 1.0
 */
@Slf4j
@Component
public class ConfirmMsgHandler extends BaseMsgHandler<ConfirmMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ConfirmMsg msg) throws Exception {
        log.info("已收到编号IMEI号为:{}的确认帧",msg.getEquipmentName());
    }
}
