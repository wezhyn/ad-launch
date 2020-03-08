package com.ad.admain.cache;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.utils.SquareUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName FreeEquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 15:11
 * @Version V1.0
 **/
@Component
/**
 * @Description //空闲车辆缓存
 * @Date 2020/3/8 15:44
 * @param null
 *@return
 **/
public class FreeEquipmentCache {
    private static final HashMap<String, Long> freeEquipMap = new HashMap<>();
    /**
     * @Description //添加空闲车辆
     * @Date 2020/3/8 15:45
     * @param imei
     * @param pooledId
     *@return com.ad.admain.controller.equipment.entity.Equipment
     **/
    public static synchronized Long setFreeEquipment(String imei,Long pooledId){
       return freeEquipMap.put(imei,pooledId);
    }
    /*
     * @Description //删除空闲车辆
     * @Date 2020/3/8 15:45
     * @param imei
     *@return com.ad.admain.controller.equipment.entity.Equipment
     **/
    public static synchronized Long remove(String imei) {

        return freeEquipMap.remove(imei);
    }

    /**
     * @Description //获取所有空闲车辆的imei集合
     * @Date 2020/3/8 15:45
     * @param
     *@return java.util.Set<java.lang.String>
     **/
    public static Set<String> getAllFreeImei(){
        return freeEquipMap.keySet();
    }
    /**
     * @Description //获取空闲车辆数
     * @Date 2020/3/8 15:46
     * @param
     *@return int
     **/
    public static int getFreeCount(){
       return freeEquipMap.keySet().size();
    }

    public static List<Equipment> getFreeEquipList(Double longitude, Double latitude,Double square){
        Double info[] = SquareUtils.getSquareInfo(longitude,latitude,square);
        List<Equipment> freeEquips = new ArrayList<>();
        for (Map.Entry<String,Long> entry :freeEquipMap.entrySet()){

        }
        return null;
    }

}
