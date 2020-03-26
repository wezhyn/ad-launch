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
                    allocatedEquip.setRest((int) (allocatedEquip.getRest() + rate));
                    allocatedEquip.releaseAllocate();
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
                if (pooledIdAndEquipCache.tryAllocate()) {
                    if (pooledIdAndEquipCache.getRest() > rate) {
                        pooledIdAndEquipCache.setRest((int) (rate - pooledIdAndEquipCache.getRest()));
                        scopeEquips.add(pooledIdAndEquipCache);
                        driverNum--;
                    }
                }
            }
        }
        return Collections.unmodifiableList(scopeEquips);
    }

}
