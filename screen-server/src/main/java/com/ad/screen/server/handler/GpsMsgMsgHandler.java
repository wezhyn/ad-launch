package com.ad.screen.server.handler;

import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.vo.req.GpsMsg;
import com.ad.screen.server.vo.req.Point2D;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

import static com.ad.screen.server.server.ScreenChannelInitializer.EQUIPMENT;

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
    @Autowired
    PooledIdAndEquipCacheService cacheService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GpsMsg msg) throws Exception {
        String imei=msg.getEquipmentName();
        log.debug("收到IMEI号为{}的GPS帧", imei);
        AdEquipment equip=ctx.channel().attr(EQUIPMENT).get();
        if (imei!=null && !"".equals(imei)) {
            //获取设备的经纬度信息
            Point2D net=msg.getNetData();
            DecimalFormat df=new DecimalFormat("0.00000");
            double x=Double.parseDouble(df.format(net.getX()/100.0));
            double y=Double.parseDouble(df.format(net.getY()/100.0));
            //更新channel内部和缓存当中的设备信息
            equip.setLongitude(x);
            equip.setLatitude(y);
            log.info("{}的地理位置已更新", msg.getEquipmentName());
        }
    }
}
