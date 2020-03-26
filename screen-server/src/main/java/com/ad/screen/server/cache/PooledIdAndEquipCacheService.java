package com.ad.screen.server.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName PooledIdAndEquipCacheService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 21:30
 * @Version V1.0
 **/
@Component
@Slf4j
public class PooledIdAndEquipCacheService extends GuavaAbstractLoadingCache<String, PooledIdAndEquipCache> implements IPooledIdAndEquipCache {
    @Autowired
    IEquipmentCache equipmentCache;


    @Override
    protected PooledIdAndEquipCache fetchData(String key) throws Exception {
        return null;
    }

    @Override
    public PooledIdAndEquipCache get(String key) {
        return null;
    }
}
