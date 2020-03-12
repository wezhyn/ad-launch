package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.screen.server.vo.req.GpsMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName GpsMsgHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 2:13
 * @Version 1.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class GpsMsgMsgHandler extends BaseMsgHandler<GpsMsg> {
    @Resource
    private RemoteEquipmentServiceI equipmentService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GpsMsg msg) throws Exception {
        Point2D net=msg.getNetData();
        double x=net.getX();
        double y=net.getY();
        AdEquipment equipment=equipmentService.loadEquipByIemi(msg.getEquipmentName());
        equipment.setLongitude(x);
        equipment.setLatitude(y);
        equipmentService.mergeEquip(equipment);
        log.info("{}的地理位置已更新", msg.getEquipmentName());
    }
}
