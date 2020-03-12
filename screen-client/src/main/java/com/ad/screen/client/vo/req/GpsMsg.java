package com.ad.screen.client.vo.req;

import javafx.geometry.Point2D;
import lombok.Data;

/**
 * @ClassName IpMsg
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/21 19:31
 * @Version 1.0
 */
@Data
public class GpsMsg extends BaseScreenRequest<Point2D> {
    public GpsMsg(Point2D netData, String equipment) {
        super(netData);
        setEquipmentName(equipment);
    }

    @Override
    public String netData() {
        Point2D p=getNetData();
        StringBuilder sb=new StringBuilder();
        sb.append(p.getX())
                .append(",")
                .append("E")
                .append(",")
                .append("N");
        return sb.toString();
    }
}
