package com.ad.admain.utils;

/**
 * @ClassName SquartUtils
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/28 22:05
 * @Version 1.0
 */
public class SquareUtils {
    public static Double[] getSquareInfo(Double longitude, Double latitude, Double square){
        double r = 6371.393;    // 地球半径千米
        double lng = longitude.doubleValue();
        double lat = latitude.doubleValue();
        double dlng = Math.abs(2 * Math.asin(Math.sin(square / (2 * r)) / Math.cos(lat * Math.PI / 180)));
        dlng = dlng * 180 / Math.PI;// 角度转为弧度
        double dlat = square / r;
        dlat = dlat * 180 / Math.PI;

        double minlng = lng - dlng;
        double maxlng = lng + dlng;
        double minlat = lat - dlat;
        double maxlat = lat + dlat;
        return new Double[]{minlng,maxlng,minlat,maxlat};
    }
}
