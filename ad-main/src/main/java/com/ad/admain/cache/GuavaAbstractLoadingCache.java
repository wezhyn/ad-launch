package com.ad.admain.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.ExecutionException;
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
public abstract class GuavaAbstractLoadingCache<K,V> {
    private int maximumSize = 1000;                 //最大缓存条数，子类在构造方法中调用setMaximumSize(int size)来更改
    private int expireAfterWriteDuration = 60;      //数据存在时长，子类在构造方法中调用setExpireAfterWriteDuration(int duration)来更改
    private TimeUnit timeUnit = TimeUnit.MINUTES;   //时间单位（分钟）
    private Date resetTime;     //Cache初始化或被重置的时间
    private long highestSize=0; //历史最高记录数
    private Date highestTime;   //创造历史记录的时间
    private LoadingCache<K, V> cache;

/**
 * @Description //通过cache.get(key)的方式来获取值
 * @Date 2020/3/7 15:47
 *@return cache实例 {@link com.google.common.cache.LoadingCache<K,V>}
 **/
    public LoadingCache<K,V> getCache(){
        if (cache==null){
            synchronized (this){
                if (cache ==null){
                    cache = CacheBuilder.newBuilder().maximumSize(maximumSize)//设置缓存最大数量
                            .expireAfterWrite(expireAfterWriteDuration,timeUnit)//设置缓存国企时间
                            .recordStats()//启用统计
                            .build(new CacheLoader<K, V>() {
                                @Override
                                public V load(K k) throws Exception {
                                    return fetchData(k);
                                }
                            });
                    this.resetTime = new Date();
                    this.highestTime = new Date();
                    log.info("本地缓存{}初始化成功",cache.getClass().getSimpleName());
                }
            }
        }
        return cache;
    }

    /**
     * @Description //根据关键字key从数据库中查询 并讲key和value一起放到缓存当中
     * @Date 2020/3/7 15:38
     * @param key 关键字
     *@return V 返回的值
     **/
    protected abstract V fetchData(K key) throws Exception;

    /**
     * @Description //根据key 获取对应的value
     * @Date 2020/3/7 15:51
     * @param key
     *@return V
     **/
    protected  V getValue(K key) throws ExecutionException{
        V result = cache.get(key);
        if (cache.size()>highestSize){
            highestSize = getCache().size();
            highestTime = new Date();
        }
        return result;
    }
}
