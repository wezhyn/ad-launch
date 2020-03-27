package com.ad.screen.server.cache;

import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 20:40
 * @Version V1.0
 **/
@Data
public class PooledIdAndEquipCache {
    /*
    设备信息
    */
    AdEquipment equipment;

    private final Object taskMapLock=new Object();
    private Channel channel;
    private HashMap<Integer, Task> taskMap;


    /**
     * 频率余量, 变动时，先调用 {@link this#tryAllocate()} 获取当前设备独占
     */
    private AtomicInteger rest;

    public PooledIdAndEquipCache(Channel channel, AdEquipment equipment) {
        this.channel=channel;
        this.equipment=equipment;
        this.rest=new AtomicInteger(ScreenChannelInitializer.SCHEDULE_NUM);
        this.taskMap=new HashMap<>(16);
    }

    /**
     * 当前设备是否已经被分配给某个设备进行任务分配
     */
    private AtomicBoolean isAllocating=new AtomicBoolean(false);

    public void restIncr(int incr) {
        while (true) {
            int old=rest.get();
            int expect=old + incr;
            if (rest.compareAndSet(old, expect)) {
                break;
            }
        }
    }

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

}
