package com.ad.admain.cache;

import com.ad.admain.controller.equipment.entity.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 20:40
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PooledIdAndEquipCache {
    /*
    * id-channel池中的唯一id号
    * */
    Long pooledId;
    /*
    设备信息
    */
    Equipment equipment;
    /**
     * 设备的工作状态  若为true则表示正在执行任务  若为false为表示设备空闲
     */
    Boolean status;

    /**
     * 频率余量
     */
    Integer rest;

}
