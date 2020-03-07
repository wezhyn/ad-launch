package com.ad.admain.cache;

/**
 * @ClassName IlocalCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 16:00
 * @Version V1.0
 **/
public interface IBaseLocalCache<K,V> {
    public V get(K key);
}
