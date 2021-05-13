package com.ad.screen.server.cache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.ad.launch.order.AdEquipment;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 20:40
 * @Version V1.0
 **/
@Slf4j
public class PooledIdAndEquipCache {

    public static final Integer MAX_SCHEDULE_NUM = 25;
    private final AtomicInteger versionCount;
    /*
    设备信息
    */
    @Getter
    private volatile AdEquipment equipment;
    @Getter
    @Setter
    private volatile boolean isChannelClose;
    @Getter
    private Channel channel;
    /**
     * 存储 channel 所要发送的 Task 帧数据：
     * {@link com.ad.screen.server.event.AllocateEventListener#onApplicationEvent} 处添加 Task(多线程环境)
     * <p>
     * {@link ScreenChannelInitializer#channelRead} 读取 Task ，定时发送给设备(EventLoop)
     * {@link com.ad.screen.server.handler.CompleteMsgHandler#channelRead} 完成时删除 Task(EventLoop)
     * {@link com.ad.screen.server.handler.CompensateHandler#channelRead} 处转移Task(EventLoop)
     * 为了保障上述两者间的同步性，
     */
    private ConcurrentMap<Integer, Task> taskMap;
    /**
     * 当前设备是否已经被分配给某个设备进行任务分配
     */
    private AtomicBoolean isAllocating = new AtomicBoolean(false);

    /**
     * 获取 Task
     *
     * @param entryId entryId
     * @return 可能返回null，由于entryId 版本不同
     */
    public Task getTask(Integer entryId) {
        return taskMap.get(entryId);
    }

    public Map<Integer, Task> getAllTask() {
        return Collections.unmodifiableMap(taskMap);
    }

    public PooledIdAndEquipCache(Channel channel, AdEquipment equipment) {
        this.channel = channel;
        this.equipment = equipment;
        this.rest = new AtomicInteger(ScreenChannelInitializer.SCHEDULE_NUM);
        this.taskMap = new ConcurrentHashMap<>(16);
        this.isChannelClose = false;
        this.versionCount = new AtomicInteger(1);
    }

    /**
     * 频率余量, 变动时，先调用 {@link this#tryAllocate()} 获取当前设备独占
     */
    private AtomicInteger rest;

    public void completeTask(int entryId) {
        taskMap.remove(entryId);
    }

    /**
     * 确保插入的Task其Key是唯一的
     *
     * @param tasks task
     */
    public void insertTask(Task tasks) throws ChannelCloseException {
        if (isChannelClose()) {
            //                分配过程中channel关闭
            throw new ChannelCloseException();
        }
        for (; ; ) {
            //            默认当前Task不会超过 SCHEDULE_NUM,version[0,24]
            int version = versionCount.getAndIncrement() % ScreenChannelInitializer.SCHEDULE_NUM;
            if (taskMap.putIfAbsent(version, tasks) == null) {
                tasks.setEntryId(version);
                break;
            }
        }
        log.info("向 IMEI 设备 : {} 安排了编号为：{} 的任务", getEquipment().getKey(), tasks.getEntryId());
    }

    public void restIncr(int incr) {
        while (true) {
            try {
                if (tryAllocate()) {
                    int old = rest.get();
                    int expect = old + incr;
                    if (rest.compareAndSet(old, expect)) {
                        break;
                    }
                }
            } finally {
                releaseAllocate();
            }
        }
    }

    public boolean tryRestOccupy(int rate) {
        try {
            if (tryAllocate()) {
                int rest = getRest();
                if (rest != 0 && rest >= rate) {
                    this.rest.compareAndSet(rest, rest - rate);
                    return true;
                }
            }
            return false;
        } finally {
            releaseAllocate();
        }
    }

    public int getRest() {
        return rest.get();
    }

    protected boolean tryAllocate() {
        return isAllocating.compareAndSet(false, true);
    }

    /**
     * 如果当前设备余量>5, 则重新让设备继续接收任务
     *
     * @return boo
     */
    protected boolean releaseAllocate() {
        return isAllocating.compareAndSet(true, false);
    }

}
