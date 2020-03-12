package com.ad.screen.client.vo;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public interface IScreenFrame {


    /**
     * 获取帧类型
     *
     * @return 帧类型
     */
    int type();


    /**
     * 设备的编号
     *
     * @return IMEI
     */
    String equipmentImei();


    /**
     * 净数据
     *
     * @return data
     */
    String netData();


}
