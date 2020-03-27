package com.ad.screen.server.cache;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName PooledIdAndEquipCacheService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 21:30
 * @Version V1.0
 **/
@Component
@Slf4j
public class PooledIdAndEquipCacheService extends AbstractGuavaLoadingCache<String, PooledIdAndEquipCache> implements IPooledIdAndEquipCache {

    @Resource
    private RemoteEquipmentServiceI equipmentService;

    protected PooledIdAndEquipCache fetchData(String key, Channel channel) throws Exception {
        try {
            final AdEquipment equip=equipmentService.loadEquipByIemi(key);
            equip.setStatus(true);
            return new PooledIdAndEquipCache(channel, equip);
        } catch (Exception e) {
            log.debug("无法根据key{}获取到值,可能是数据库中不存在该编号", key);
            throw e;
        }
    }

    /**
     * 初始化(仅一次)
     *
     * @param channel 设备对应的channel
     * @return cache
     */
    public PooledIdAndEquipCache getOrInit(String key, Channel channel) throws Exception {
        return getCache().get(key, ()->fetchData(key, channel));
    }

    @Override
    public PooledIdAndEquipCache get(String key) {
        final PooledIdAndEquipCache present=getCache().getIfPresent(key);
        if (present==null) {
            throw new RuntimeException("不允许在未初始化状态获取缓存");
        }
        return present;
    }
}
