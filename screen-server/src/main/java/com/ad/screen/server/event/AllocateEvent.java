package com.ad.screen.server.event;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.entity.EquipTask;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * EquipTask 派送
 * 简单起见：发布事件时，先监测是否有可用设备,于此进行不同的处理
 * 使用调用者线程进行事件处理，发送此事件需要手动处理如下异常：
 * {@link com.ad.screen.server.cache.ChannelCloseException}
 *
 * @author wezhyn
 * @since 03.27.2020
 */
@Getter
public class AllocateEvent extends ApplicationEvent {


    private AllocateType type;

    /**
     * 具体派送的任务
     */
    private EquipTask equipTask;

    private List<PooledIdAndEquipCache> pooledIdAndEquipCaches;

    public AllocateEvent(Object source, AllocateType type, EquipTask equipTask, List<PooledIdAndEquipCache> pooledIdAndEquipCaches) {
        super(source);
        this.type=type;
        this.equipTask=equipTask;
        this.pooledIdAndEquipCaches=pooledIdAndEquipCaches;
    }
}
