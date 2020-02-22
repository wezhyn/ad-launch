package com.ad.admain.screen.handler;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.screen.vo.req.GpsMsg;
import io.netty.channel.ChannelHandlerContext;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
public class GpsMsgHandler extends BaseHandler<GpsMsg>{
    @Autowired
    EquipmentService equipmentService;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GpsMsg msg) throws Exception {
        Point2D net = msg.getNetData();
        double x = net.getX();
        double y = net.getY();
        Equipment equipment = equipmentService.findEquipmentByIMEI(msg.getEquipmentName());
        equipment.setLongitude(x);
        equipment.setLatitude(y);
        equipmentService.save(equipment);
        log.info("{}的地理位置已更新",msg.getEquipmentName());
    }
}
