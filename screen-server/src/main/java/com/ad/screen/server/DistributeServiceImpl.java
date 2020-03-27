package com.ad.screen.server;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.SquareUtils;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.mq.DistributeTaskI;
import com.ad.screen.server.mq.PrepareTaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName DistributeServiceImpl
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/17 15:10
 * @Version V1.0
 **/
@Service
public class DistributeServiceImpl implements DistributeTaskI {
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;


    @Override
    public List<PooledIdAndEquipCache> availableEquips(PrepareTaskMessage taskMessage) {
        List<PooledIdAndEquipCache> scopeEquips=new ArrayList<>(8);
        double rate=taskMessage.getRate();
        int driverNum=taskMessage.getDeliverNum();
        Double[] info=SquareUtils.getSquareInfo(taskMessage.getLongitude(), taskMessage.getLatitude(), rate);
        ConcurrentMap<String, PooledIdAndEquipCache> cache=pooledIdAndEquipCacheService.getCache().asMap();
        final Iterator<Map.Entry<String, PooledIdAndEquipCache>> cacheEntry=cache.entrySet().iterator();
        while (driverNum > 0) {
            if (!cacheEntry.hasNext()) {
//                把分配到的设备回放
                for (PooledIdAndEquipCache allocatedEquip : scopeEquips) {
                    try {
                        while (!allocatedEquip.tryAllocate()) {
                        }
                        allocatedEquip.getRest().getAndAdd((int) rate);
                    } finally {
                        allocatedEquip.releaseAllocate();
                    }
                }
                break;
            }
            final Map.Entry<String, PooledIdAndEquipCache> entry=cacheEntry.next();
            PooledIdAndEquipCache pooledIdAndEquipCache=entry.getValue();
            AdEquipment equipment=entry.getValue().getEquipment();
            double lgt=SquareUtils.format(equipment.getLongitude());
            double lat=SquareUtils.format(equipment.getLatitude());
//            检查范围信息是否合理
            if (lgt > info[0] && lgt < info[1] && lat > info[2] && lat < info[3]) {
//                尝试获取当前设备独占
                try {
                    if (pooledIdAndEquipCache.tryAllocate()) {
                        int rest=pooledIdAndEquipCache.getRest().get();
                        if (rest > rate) {
                            pooledIdAndEquipCache.getRest().compareAndSet(rest, (int) (rest - rate));
                            scopeEquips.add(pooledIdAndEquipCache);
                            driverNum--;
                        }
                    }
                } finally {
                    pooledIdAndEquipCache.releaseAllocate();
                }
            }
        }
        return Collections.unmodifiableList(scopeEquips);
    }


    @Override
    public Optional<PooledIdAndEquipCache> availableSingleEquip(PrepareTaskMessage taskMessage) {
        final List<PooledIdAndEquipCache> equipCaches=availableEquips(taskMessage);
        return equipCaches.size()==1 ? Optional.of(equipCaches.get(0)) : Optional.empty();
    }
}
