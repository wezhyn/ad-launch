package com.ad.screen.server.service;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.entity.EquipTask;

import java.util.List;

/**
 * @author wezhyn
 * @since 03.30.2020
 */
public interface DistributeTaskService {

    /**
     * 1. 检查车辆是否充足
     * 2. 插入数据
     *
     * @param equipTask 订单信息
     * @return
     */
    List<PooledIdAndEquipCache> saveAndCheckOrder(EquipTask equipTask);

}
