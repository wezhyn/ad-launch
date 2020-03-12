package com.ad.screen.server.cache;

import com.ad.launch.order.exception.NotEquipmentException;

/**
 * @ClassName IlocalCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 16:00
 * @Version V1.0
 **/
public interface IBaseLocalCache<K, V> {
    public V get(K key) throws NotEquipmentException;
}
