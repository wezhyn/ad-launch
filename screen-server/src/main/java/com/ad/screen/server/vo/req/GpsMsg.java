package com.ad.screen.server.vo.req;

import com.ad.screen.server.vo.FrameType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @ClassName IpMsg
 * @Description TODO
 * @Author ZLB
 * @Date 2020/2/21 19:31
 * @Version 1.0
 */
@Getter
@EqualsAndHashCode(callSuper=false)
public class GpsMsg extends BaseScreenRequest<Point2D> {

    public GpsMsg(String equipmentName, Point2D netData) {
        super(equipmentName, FrameType.GPS, netData);
    }
}
