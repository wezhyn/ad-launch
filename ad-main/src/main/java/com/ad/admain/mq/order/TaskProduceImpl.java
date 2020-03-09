package com.ad.admain.mq.order;

import com.ad.admain.cache.PooledIdAndEquipCache;
import com.ad.admain.cache.PooledIdAndEquipCacheService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.screen.entity.Task;
import com.ad.admain.utils.SquareUtils;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @ClassName TaskProducer
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 0:57
 * @Version V1.0
 **/
@Service
@Slf4j
public class TaskProduceImpl implements DistributeTaskI {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;

    @Override
    public void sendTask(AdOrder adOrder) {
        Integer rate = adOrder.getRate();
        Integer deliverNum = adOrder.getDeliverNum();
        Integer numPerEquip = adOrder.getNumPerEquip();
        Integer numPerTask = numPerEquip / rate;
        int onlinenum = pooledIdAndEquipCacheService.count();
        if (onlinenum < deliverNum) {
            log.debug("目前没有这么多的在线车辆数");
        } else {
            HashMap<Long,PooledIdAndEquipCache> freeEquips = freeEquips();
            int freeCount = freeEquips.size();
            if (freeCount < deliverNum) {
                log.debug("目前空闲车数为:{},小于投放车辆数{}", freeCount, deliverNum);
            }
            HashMap<Long,PooledIdAndEquipCache> available = scopeAvailableFreeEquips(adOrder.getLongitude(),adOrder.getLatitude(),adOrder.getScope(),adOrder.getRate());
            if (available.size()<deliverNum){
                log.debug("当前区域内可用车辆数目为{}小于{}",available.size(),deliverNum);
            }
            Set<Map.Entry<Long, PooledIdAndEquipCache>> channelIds = available.entrySet();
            List<Task> taskList = new ArrayList<>();
            Iterator<Map.Entry<Long, PooledIdAndEquipCache>> iterator = channelIds.iterator();
            int i = 1;
            while (iterator.hasNext()){
                Map.Entry<Long, PooledIdAndEquipCache> entry = iterator.next();
                Cache<String,PooledIdAndEquipCache> cache = pooledIdAndEquipCacheService.getCache();
                PooledIdAndEquipCache pooledIdAndEquipCache = cache.getIfPresent(entry.getValue().getEquipment().getKey());
                try {
                    pooledIdAndEquipCache.setRest(pooledIdAndEquipCache.getRest()-rate);
                    Task task = Task.builder()
                            .adOrderId(adOrder.getId())
                            .entryId(i++)
                            .repeatNum(numPerTask)
                            .pooledId(pooledIdAndEquipCache.getPooledId())
                            .status(false)
                            .verticalView(adOrder.getProduce().getVertical())
                            .build();
                    taskList.add(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rocketMQTemplate.asyncSend("task_topic",taskList,new CommonSendCallback<List<Task>>(taskList));
        }
    }




    @Override
    public HashMap<Long, PooledIdAndEquipCache> freeEquips() {
        HashMap<Long, PooledIdAndEquipCache> equipmentHashMap = new HashMap<>();
        for (Map.Entry<String, PooledIdAndEquipCache> entry : pooledIdAndEquipCacheService.getCache().asMap().entrySet()) {
            PooledIdAndEquipCache cache = entry.getValue();
            if (entry.getValue().getStatus() == false) {
                equipmentHashMap.put(cache.getPooledId(), cache);
            }
        }
        return equipmentHashMap;
    }

    @Override
    public HashMap<Long, PooledIdAndEquipCache> scopeFreeEquips(Double longitude, Double latitude, Double scope) {
        Double[] info = SquareUtils.getSquareInfo(longitude,latitude,scope);
        HashMap<Long,PooledIdAndEquipCache> freeEquips = freeEquips();
        for (Map.Entry<Long,PooledIdAndEquipCache> entry: freeEquips.entrySet()
             ) {
            PooledIdAndEquipCache pooledIdAndEquipCache = entry.getValue();
            Equipment equipment = entry.getValue().getEquipment();
            Double lgt = equipment.getLongitude();
            Double lat = equipment.getLatitude();
            if (lgt>=info[0]&&lgt<=info[1]&&lat>=info[2]&&lat<=info[3]){
                freeEquips.put(pooledIdAndEquipCache.getPooledId(),pooledIdAndEquipCache);
            }
        }
        return freeEquips;
    }

    @Override
    public HashMap<Long, PooledIdAndEquipCache> scopeAvailableFreeEquips(Double longitude, Double latitude, Double scope, int rate) {
        Double[] info = SquareUtils.getSquareInfo(longitude,latitude,scope);
        HashMap<Long,PooledIdAndEquipCache> freeEquips = freeEquips();
        for (Map.Entry<Long,PooledIdAndEquipCache> entry: freeEquips.entrySet()
        ) {

            PooledIdAndEquipCache pooledIdAndEquipCache = entry.getValue();
            if (pooledIdAndEquipCache.getRest()<rate)
                continue;
            Equipment equipment = entry.getValue().getEquipment();
            Double lgt = equipment.getLongitude();
            Double lat = equipment.getLatitude();
            if (lgt>=info[0]&&lgt<=info[1]&&lat>=info[2]&&lat<=info[3]){
                freeEquips.put(pooledIdAndEquipCache.getPooledId(),pooledIdAndEquipCache);
            }
        }
        return freeEquips;    }
}
