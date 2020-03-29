package com.ad.screen.server.cache;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName GuavaAbstractLoadingCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 15:29
 * @Version V1.0
 **/
@Slf4j
@Data
public abstract class AbstractGuavaLoadingCache<K, V> {
    private int maximumSize=1000;                 //最大缓存条数，子类在构造方法中调用setMaximumSize(int size)来更改
    private int expireAfterWriteDuration=60;      //数据存在时长，子类在构造方法中调用setExpireAfterWriteDuration(int duration)来更改
    private TimeUnit timeUnit=TimeUnit.MINUTES;   //时间单位（分钟）
    private Date resetTime;     //Cache初始化或被重置的时间
    private long highestSize=0; //历史最高记录数
    private Date highestTime;   //创造历史记录的时间
    private final Cache<K, V> cache;


    public AbstractGuavaLoadingCache() {
        this.cache=CacheBuilder.newBuilder().maximumSize(maximumSize)//设置缓存最大数量
                .expireAfterWrite(expireAfterWriteDuration, timeUnit)//设置缓存国企时间
                .recordStats()//启用统计
                .build();
        this.resetTime=new Date();
        this.highestTime=new Date();
    }

    /**
     * 统计缓存数据
     *
     * @return java.lang.Integer
     **/
    public Integer count() {
        return cache.asMap().size();
    }

    public void remove(K key) {
        getCache().invalidate(key);
    }


}
