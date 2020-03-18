package com.ad.screen.server;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.SquareUtils;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.exception.InsufficientException;
import com.ad.screen.server.mq.DistributeTaskI;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public HashMap<Long, PooledIdAndEquipCache> scopeEquips(Double longitude, Double latitude, Double scope) {
        HashMap<Long,PooledIdAndEquipCache> scopeEquips = new HashMap<>();

        Double[] info = SquareUtils.getSquareInfo(longitude, latitude, scope);
        ConcurrentMap<String, PooledIdAndEquipCache> cache = pooledIdAndEquipCacheService.getCache().asMap();
        for (Map.Entry<String, PooledIdAndEquipCache> entry:cache.entrySet()){
            PooledIdAndEquipCache pooledIdAndEquipCache=entry.getValue();
            AdEquipment equipment=entry.getValue().getEquipment();
            Double lgt=equipment.getLongitude();
            Double lat=equipment.getLatitude();
            if (lgt >= info[0] && lgt <= info[1] && lat >= info[2] && lat <= info[3]) {
                scopeEquips.put(pooledIdAndEquipCache.getPooledId(), pooledIdAndEquipCache);
            }
        }
        return scopeEquips;    }

    @Override
    public HashMap<Long, PooledIdAndEquipCache> scopeAvailableFreeEquips(Double longitude, Double latitude, Double scope, int rate) {

            HashMap<Long, PooledIdAndEquipCache> freeEquips=scopeEquips(longitude,latitude,scope);
            if (freeEquips==null){
                throw new InsufficientException("目前没有这么多的车辆");
            }


            HashMap<Long, PooledIdAndEquipCache> availableEquips = new HashMap<>();
            for (Map.Entry<Long, PooledIdAndEquipCache> entry : freeEquips.entrySet()
            ) {

                PooledIdAndEquipCache pooledIdAndEquipCache=entry.getValue();
                //比较目前车辆的剩余频率和订单的频率要求,若小于则跳过检查
                if (pooledIdAndEquipCache.getRest() < rate) {
                    continue;
                }

                availableEquips.put(pooledIdAndEquipCache.getPooledId(), pooledIdAndEquipCache);
            }
            return availableEquips;


    }


}
