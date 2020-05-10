package com.ad.launch.order;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @ClassName SquartUtils
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/28 22:05
 * @Version 1.0
 */
public class SquareUtils {
    public static Double[] getSquareInfo(Double longitude, Double latitude, Double square) {
        DecimalFormat df4 = new DecimalFormat("##.0000");
        double r = 6371.393;    // 地球半径千米
        double lng = longitude.doubleValue();
        double lat = latitude.doubleValue();
        double dlng = 2 * Math.asin(Math.sin(square / (2 * r)) / Math.cos(lat * Math.PI / 180));
        dlng = dlng * 180 / Math.PI;// 角度转为弧度
        double dlat = square / r;
        dlat = dlat * 180 / Math.PI;

        // 保持正值
        dlng = Math.abs(dlng);
        dlat = Math.abs(dlat);
        double minlng = format(lng - dlng);
        double maxlng = format(lng + dlng);
        double minlat = format(lat - dlat);
        double maxlat = format(lat + dlat);
        Double[] info = new Double[]{minlng, maxlng, minlat, maxlat};
        return info;
    }


    public static double format(double val){
        double format = new BigDecimal(val).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        return format;
    }
}
