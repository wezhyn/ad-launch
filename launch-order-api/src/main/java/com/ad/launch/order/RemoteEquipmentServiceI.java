package com.ad.launch.order;

import com.ad.launch.order.exception.NotEquipmentException;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
public interface RemoteEquipmentServiceI {

    /**
     * 加载设备基本信息
     *
     * @param iemi 设备编号
     * @return equipment
     */
    AdEquipment loadEquipByIemi(String iemi) throws NotEquipmentException;


    /**
     * 消费端更新设备信息
     *
     * @param equipment equipment
     * @return equipment
     */
    AdEquipment mergeEquip(AdEquipment equipment);

}
