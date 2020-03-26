package com.ad.screen.server.cache;

import com.ad.launch.order.AdEquipment;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 20:40
 * @Version V1.0
 **/
@Data
public class PooledIdAndEquipCache {
    /**
     * id-channel池中的唯一id号
     */
    Long pooledId;
    /*
    设备信息
    */
    AdEquipment equipment;
//    /**
//     * 设备的工作状态  若为true则表示正在执行任务  若为false为表示设备空闲
//     */
//    Boolean status;

    /**
     * 频率余量
     */
    Integer rest;

    public PooledIdAndEquipCache(Long pooledId, AdEquipment equipment, Integer rest) {
        this.pooledId=pooledId;
        this.equipment=equipment;
        this.rest=rest;
    }

    /**
     * 当前设备是否已经被分配给某个设备进行任务分配
     */
    private AtomicBoolean isAllocating=new AtomicBoolean(false);


    public boolean tryAllocate() {
        return isAllocating.compareAndSet(false, true);
    }

    /**
     * 如果当前设备余量>5, 则重新让设备继续接收任务
     *
     * @return boo
     */
    public boolean releaseAllocate() {
        return isAllocating.compareAndSet(true, false);
    }


    public static final class PooledIdAndEquipCacheBuilder {
        Long pooledId;
        /**
         * 设备信息
         */
        AdEquipment equipment;
        Integer rest;

        private PooledIdAndEquipCacheBuilder() {
        }

        public static PooledIdAndEquipCacheBuilder aPooledIdAndEquipCache() {
            return new PooledIdAndEquipCacheBuilder();
        }

        public PooledIdAndEquipCacheBuilder withPooledId(Long pooledId) {
            this.pooledId=pooledId;
            return this;
        }

        public PooledIdAndEquipCacheBuilder withEquipment(AdEquipment equipment) {
            this.equipment=equipment;
            return this;
        }

        public PooledIdAndEquipCacheBuilder withRest(Integer rest) {
            this.rest=rest;
            return this;
        }

        public PooledIdAndEquipCache build() {
            PooledIdAndEquipCache pooledIdAndEquipCache=new PooledIdAndEquipCache();
            pooledIdAndEquipCache.setPooledId(pooledId);
            pooledIdAndEquipCache.setEquipment(equipment);
            pooledIdAndEquipCache.setRest(rest);
            return pooledIdAndEquipCache;
        }
    }
}
