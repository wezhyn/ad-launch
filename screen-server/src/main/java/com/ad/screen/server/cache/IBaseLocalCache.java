package com.ad.screen.server.cache;

/**
 * @ClassName IlocalCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 16:00
 * @Version V1.0
 **/
public interface IBaseLocalCache<K, V> {
    /**
     * 根据 Key 获取对应 Valuel
     *
     * @param key Key
     * @return V or Null
     */
    V get(K key);
}
