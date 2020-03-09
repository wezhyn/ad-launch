package com.ad.admain.cache;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName PooledIdAndEquipCacheService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 21:30
 * @Version V1.0
 **/
@Component
@Slf4j
public class PooledIdAndEquipCacheService extends GuavaAbstractLoadingCache<String,PooledIdAndEquipCache> implements IPooledIdAndEquipCache {
   @Autowired
   IEquipmentCache equipmentCache;

//    @Override
//    public Cache<String,PooledIdAndEquipCache> getCache(){
//        if (super.getCache()==null){
//            synchronized (this){
//                if (super.getCache() ==null){
//                    Cache<String,PooledIdAndEquipCache> cache = CacheBuilder.newBuilder().maximumSize(super.getMaximumSize())//设置缓存最大数量
//                            .expireAfterWrite(super.getExpireAfterWriteDuration(),super.getTimeUnit())//设置缓存国企时间
//                            .recordStats()//启用统计
//                            .build();
//                    super.setCache(cache);
//                    super.setResetTime(new Date());
//                    super.setHighestTime(new Date());
//                    log.info("本地缓存{}初始化成功",cache.getClass().getSimpleName());
//                }
//            }
//        }
//        return super.getCache();
//    }
    @Override
    protected PooledIdAndEquipCache fetchData(String key) throws Exception {
       return null;
    }

    @Override
    public PooledIdAndEquipCache get(String key) {
        return null;
    }
}
