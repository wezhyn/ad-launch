package com.ad.screen.server.mq;

import com.ad.launch.order.TaskMessage;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
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
        topic="task_message_topic",
        consumerGroup="task_message_consumers",
        selectorExpression="*"
)
@Component
@Slf4j
public class PayMessageListener implements RocketMQListener<TaskMessage> {
    @Autowired
    DistributeTaskI distributeTaskI;
    @Autowired
    IdChannelPool idChannelPool;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;

    @Override
    public void onMessage(TaskMessage taskMessage) {
        log.info("收到id为:{}成功支付消息", taskMessage.getOid());
        Integer rate = taskMessage.getRate();
        Integer deliverNum = taskMessage.getDeliverNum();
        Integer numPerEquip = taskMessage.getNumPerEquip();
        Integer numPerTask = numPerEquip / rate;
        int onlinenum = pooledIdAndEquipCacheService.count();

        //目前没有这么多的在线车辆数,退出
        if (onlinenum < deliverNum) {
            log.debug("目前没有这么多的在线车辆数");
            return;
        } else {
            //目前区域内可用符合订单要求的车辆数小于订单要求投放的车辆数，退出
            HashMap<Long, PooledIdAndEquipCache> available = distributeTaskI.scopeAvailableFreeEquips(taskMessage.getLongitude(), taskMessage.getLatitude(), taskMessage.getScope(), taskMessage.getRate());
            if (available.size() < deliverNum) {
                log.info("当前区域内可用车辆数目为{}小于{}", available.size(), deliverNum);
                return;
            }


            //区域内的车辆数满足订单要求，开始往channel中分配task
            Set<Map.Entry<Long, PooledIdAndEquipCache>> availableCache = available.entrySet();
//            List<Task> taskList=new ArrayList<>();
            Iterator<Map.Entry<Long, PooledIdAndEquipCache>> iterator = availableCache.iterator();
            int count = deliverNum;
            while (count-- > 0 && iterator.hasNext()) {

                Map.Entry<Long, PooledIdAndEquipCache> entry = iterator.next();
//                Cache<String, PooledIdAndEquipCache> cache=pooledIdAndEquipCacheService.getCache();
                PooledIdAndEquipCache pooledIdAndEquipCache = entry.getValue();
                Long pooledId = entry.getKey();

                //组合广告内容
                List<String> views = taskMessage.getProduceContext();
                StringBuilder sb = new StringBuilder();
                for (String str : views) {
                    sb.append(str);
                }

                try {
                    Integer entryId = null;
                    Channel channel = idChannelPool.getChannel(entry.getKey());
                    //取出channel已经收到的任务列表
                    HashMap<Integer,Task> received = channel.attr(ScreenChannelInitializer.TASK_MAP).get();
                    if (received == null) {
                        entryId = 1;
                        received = new HashMap<>();
                        //单个设备内添加rate个数个task
                        for (int i = 1; i <= rate; i++) {
                            //将任务加入对应channel的received列表中
                            Task task = Task.builder()
                                    .oid(taskMessage.getOid())
                                    .entryId(entryId++)
                                    .repeatNum(numPerTask)
                                    .sendIf(false)
                                    .uid(taskMessage.getUid())
                                    .view(sb.toString())
                                    .verticalView(taskMessage.getVertical())
                                    .build();
                            received.put(i,task);
                        }
                    }else {
                        Set<Integer> keySet  = received.keySet();
                        int addNum = rate.intValue();
                        for (int j = 1;addNum>0&&j<=25;j++){
                            if (!keySet.contains(j)){
                                //将任务加入对应channel的received列表中
                                Task task = Task.builder()
                                        .oid(taskMessage.getOid())
                                        .entryId(j)
                                        .repeatNum(numPerTask)
                                        .sendIf(false)
                                        .uid(taskMessage.getUid())
                                        .view(sb.toString())
                                        .verticalView(taskMessage.getVertical())
                                        .build();
                                received.put(j,task);
                                addNum--;
                            }
                        }
                    }

                    log.info("已经往pooledId为:{}的channel中安排了{}个task", pooledId, rate);

                    //设置新的频率余量后更新缓存信息
                    String imei = entry.getValue().getEquipment().getKey();
                    pooledIdAndEquipCache.setRest(pooledIdAndEquipCache.getRest() - rate);
                    channel.attr(ScreenChannelInitializer.TASK_MAP).set(received);
                    pooledIdAndEquipCacheService.setValue(imei, pooledIdAndEquipCache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
