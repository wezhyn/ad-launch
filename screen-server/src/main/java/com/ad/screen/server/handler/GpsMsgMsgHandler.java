package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.vo.req.GpsMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    PooledIdAndEquipCacheService cacheService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GpsMsg msg) throws Exception {
        String imei = msg.getEquipmentName();
        log.debug("收到IMEI号为{}的GPS帧",imei);
        //获取channel的pooled id
        Long pooledId = ctx.channel().attr(ScreenChannelInitializer.REGISTERED_ID).get();
        AdEquipment equip = ctx.channel().attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).get();
        if (pooledId!=null&&imei!=null&&imei!=""){
            //获取设备的经纬度信息
            Point2D net=msg.getNetData();
            double x=net.getX();
            double y=net.getY();
//            AdEquipment equipment=equipmentService.loadEquipByIemi(msg.getEquipmentName());
//            equipment.setLongitude(x);
//            equipment.setLatitude(y);

            //获取缓存内该设备对应的信息
            PooledIdAndEquipCache cache = cacheService.getValue(imei);
            //更新channel内部和缓存当中的设备信息
            equip.setLongitude(x/100);
            equip.setLatitude(y/100);
            cache.setEquipment(equip);
            ctx.channel().attr(ScreenProtocolCheckInboundHandler.EQUIPMENT).set(equip);
            cacheService.setValue(imei,cache);
            log.info("{}的地理位置已更新", msg.getEquipmentName());
        }
    }
}
