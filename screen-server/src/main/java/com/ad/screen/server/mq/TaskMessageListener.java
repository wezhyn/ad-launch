package com.ad.screen.server.mq;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.AdOrder;
import com.ad.launch.order.SquareUtils;
import com.ad.launch.order.TaskMessage;
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
public class TaskMessageListener implements RocketMQListener<TaskMessage>,DistributeTaskI{
    @Autowired
    IdChannelPool idChannelPool;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;

    @Override
    public void onMessage(TaskMessage taskMessage) {
        log.info("收到id为:{}成功支付消息",taskMessage.getOid() );
        Integer rate=taskMessage.getRate();
        Integer deliverNum=taskMessage.getDeliverNum();
        Integer numPerEquip=taskMessage.getNumPerEquip();
        Integer numPerTask=numPerEquip/rate;
        int onlinenum=pooledIdAndEquipCacheService.count();

        //目前没有这么多的在线车辆数,退出
        if (onlinenum < deliverNum) {
            log.debug("目前没有这么多的在线车辆数");
            return;
        } else {
            //目前的空闲车辆小于订单投放的车辆数，退出
            HashMap<Long, PooledIdAndEquipCache> freeEquips=freeEquips();
            int freeCount=freeEquips.size();
            if (freeCount < deliverNum) {
                log.info("目前空闲车数为:{},小于投放车辆数{}", freeCount, deliverNum);
                return;
            }
            //目前区域内可用符合订单要求的车辆数小于订单要求投放的车辆数，退出
            HashMap<Long, PooledIdAndEquipCache> available=scopeAvailableFreeEquips(taskMessage.getLongitude(), taskMessage.getLatitude(), taskMessage.getScope(), taskMessage.getRate());
            if (available.size() < deliverNum) {
                log.info("当前区域内可用车辆数目为{}小于{}", available.size(), deliverNum);
                return;
            }


            //区域内的车辆数满足订单要求，开始往channel中分配task
            Set<Map.Entry<Long, PooledIdAndEquipCache>> availableCache=available.entrySet();
//            List<Task> taskList=new ArrayList<>();
            Iterator<Map.Entry<Long, PooledIdAndEquipCache>> iterator=availableCache.iterator();
            int count = deliverNum;
            while (count-->0 && iterator.hasNext()) {

                Map.Entry<Long, PooledIdAndEquipCache> entry=iterator.next();
//                Cache<String, PooledIdAndEquipCache> cache=pooledIdAndEquipCacheService.getCache();
                PooledIdAndEquipCache pooledIdAndEquipCache= entry.getValue();
                Long pooledId = entry.getKey();
                try {
                    Integer entryId = null;
                    Channel channel = idChannelPool.getChannel(entry.getKey());
                    //取出channel已经收到的任务列表
                    List<Task> received = channel.attr(ScreenChannelInitializer.TASK_LIST).get();
                    if (received==null){
                        entryId = 1;
                        received = new ArrayList<>();
                    }else {
                        entryId = received.size();
                    }
                    List<String> views = taskMessage.getProduceContext();
                    StringBuilder sb = new StringBuilder();
                    for (String str : views){
                        sb.append(str);
                    }


                    //单个设备内添加rate个数个task
                    for (int i = 1; i <=rate ; i++) {
                        //将任务加入对应channel的received列表中
                        Task task=Task.builder()
                                .adOrderId(taskMessage.getOid())
                                .entryId(entryId++)
                                .repeatNum(numPerTask)
                                .pooledId(pooledIdAndEquipCache.getPooledId())
                                .status(false)
                                .view(sb.toString())
                                .verticalView(taskMessage.getVertical())
                                .build();
                        received.add(task);
                    }
                    log.info("已经往pooledId为:{}的channel中安排了{}个task",pooledId,rate);

                    //设置新的频率余量后更新缓存信息
                    String imei = entry.getValue().getEquipment().getKey();
                    pooledIdAndEquipCache.setRest(pooledIdAndEquipCache.getRest() - rate);
                    channel.attr(ScreenChannelInitializer.TASK_LIST).set(received);
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
