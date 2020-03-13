package com.ad.screen.server.mq;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.AdOrder;
import com.ad.launch.order.SquareUtils;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.google.common.cache.Cache;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description //mq任务消息监听
 * @Date 2020/3/6 23:15
 **/
@RocketMQMessageListener(
        topic="task_topic",
        consumerGroup="task_consumers",
        selectorExpression="*"
)
@Component
@Slf4j
public class TaskMessageListener implements RocketMQListener<AdOrder>,DistributeTaskI{
    @Autowired
    IdChannelPool idChannelPool;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;

    @Override
    public void onMessage(AdOrder adOrder) {
        log.debug("收到id为:{}成功支付消息",adOrder.getId() );
        Integer rate=adOrder.getRate();
        Integer deliverNum=adOrder.getDeliverNum();
        Integer numPerEquip=adOrder.getNumPerEquip();
        Integer numPerTask=numPerEquip/rate;
        int onlinenum=pooledIdAndEquipCacheService.count();
        if (onlinenum < deliverNum) {
            log.debug("目前没有这么多的在线车辆数");
        } else {
            HashMap<Long, PooledIdAndEquipCache> freeEquips=freeEquips();
            int freeCount=freeEquips.size();
            if (freeCount < deliverNum) {
                log.debug("目前空闲车数为:{},小于投放车辆数{}", freeCount, deliverNum);
            }
            HashMap<Long, PooledIdAndEquipCache> available=scopeAvailableFreeEquips(adOrder.getLongitude(), adOrder.getLatitude(), adOrder.getScope(), adOrder.getRate());
            if (available.size() < deliverNum) {
                log.debug("当前区域内可用车辆数目为{}小于{}", available.size(), deliverNum);
            }
            Set<Map.Entry<Long, PooledIdAndEquipCache>> channelIds=available.entrySet();



//            List<Task> taskList=new ArrayList<>();
            Iterator<Map.Entry<Long, PooledIdAndEquipCache>> iterator=channelIds.iterator();

            while (iterator.hasNext()) {
                Map.Entry<Long, PooledIdAndEquipCache> entry=iterator.next();
//                Cache<String, PooledIdAndEquipCache> cache=pooledIdAndEquipCacheService.getCache();
                PooledIdAndEquipCache pooledIdAndEquipCache= entry.getValue();
                try {
                    Integer entryId = null;
                    Channel channel = idChannelPool.getChannel(entry.getKey());
                    //取出channel已经收到的任务列表
                    List<Task> received = channel.attr(ScreenChannelInitializer.TASK_LIST).get();
                    if (received==null){
                        entryId = 1;
                    }else {
                        entryId = received.size();
                    }
                    //将任务加入对应channel的received列表中
                    Task task=Task.builder()
                            .adOrderId(adOrder.getId())
                            .entryId(entryId++)
                            .repeatNum(numPerTask)
                            .pooledId(pooledIdAndEquipCache.getPooledId())
                            .status(false)
                            .verticalView(adOrder.getProduce().getVertical())
                            .build();
                    received.add(task);
                    //设置新的频率余量后更新缓存信息
                    String imei = entry.getValue().getEquipment().getKey();
                    pooledIdAndEquipCache.setRest(pooledIdAndEquipCache.getRest() - rate);
                    pooledIdAndEquipCacheService.setValue(imei,pooledIdAndEquipCache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        for (int i=0; i < tasks.size(); i++) {
//            received.add(tasks.get(i));
//        }
//        channel.attr(ScreenChannelInitializer.TASK_LIST).set(received);
    }

    @Override
    public HashMap<Long, PooledIdAndEquipCache> freeEquips() {
        HashMap<Long, PooledIdAndEquipCache> equipmentHashMap=new HashMap<>();
        for (Map.Entry<String, PooledIdAndEquipCache> entry : pooledIdAndEquipCacheService.getCache().asMap().entrySet()) {
            PooledIdAndEquipCache cache=entry.getValue();
            if (!entry.getValue().getStatus()) {
                equipmentHashMap.put(cache.getPooledId(), cache);
            }
        }
        return equipmentHashMap;
    }

    @Override
    public HashMap<Long, PooledIdAndEquipCache> scopeFreeEquips(Double longitude, Double latitude, Double scope) {
        Double[] info= SquareUtils.getSquareInfo(longitude, latitude, scope);
        HashMap<Long, PooledIdAndEquipCache> freeEquips=freeEquips();
        for (Map.Entry<Long, PooledIdAndEquipCache> entry : freeEquips.entrySet()
        ) {
            PooledIdAndEquipCache pooledIdAndEquipCache=entry.getValue();
            AdEquipment equipment=entry.getValue().getEquipment();
            Double lgt=equipment.getLongitude();
            Double lat=equipment.getLatitude();
            if (lgt >= info[0] && lgt <= info[1] && lat >= info[2] && lat <= info[3]) {
                freeEquips.put(pooledIdAndEquipCache.getPooledId(), pooledIdAndEquipCache);
            }
        }
        return freeEquips;
    }


    //获取区域范围内符合调度要求的车辆缓存
    @Override
    public HashMap<Long, PooledIdAndEquipCache> scopeAvailableFreeEquips(Double longitude, Double latitude, Double scope, int rate) {
        Double[] info=SquareUtils.getSquareInfo(longitude, latitude, scope);
        HashMap<Long, PooledIdAndEquipCache> freeEquips=freeEquips();
        HashMap<Long, PooledIdAndEquipCache> availableEquips = new HashMap<>();
        for (Map.Entry<Long, PooledIdAndEquipCache> entry : freeEquips.entrySet()
        ) {

            PooledIdAndEquipCache pooledIdAndEquipCache=entry.getValue();
            //比较目前车辆的剩余频率和订单的频率要求,若小于则跳过检查
            if (pooledIdAndEquipCache.getRest() < rate) {
                continue;
            }
            AdEquipment equipment=entry.getValue().getEquipment();
            Double lgt=equipment.getLongitude();
            Double lat=equipment.getLatitude();
            if (lgt >= info[0] && lgt <= info[1] && lat >= info[2] && lat <= info[3]) {
                availableEquips.put(pooledIdAndEquipCache.getPooledId(), pooledIdAndEquipCache);
            }
        }
        return availableEquips;
    }
}
