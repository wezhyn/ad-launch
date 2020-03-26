package com.ad.screen.server.mq;

import com.ad.launch.order.TaskMessage;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.exception.InsufficientException;
import com.ad.screen.server.server.ScreenChannelInitializer;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 支付成功订单消费
 *
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
        Integer rate=taskMessage.getRate();
        Integer deliverNum=taskMessage.getDeliverNum();
        int onlinenum=pooledIdAndEquipCacheService.count();

        //目前没有这么多的在线车辆数,退出
        if (onlinenum < deliverNum) {
            throw new InsufficientException("目前没有这么多的在线车辆数");
        } else {
            //目前区域内可用符合订单要求的车辆数小于订单要求投放的车辆数，退出
            List<PooledIdAndEquipCache> available=distributeTaskI.availableEquips(new TaskMessageAdapter(taskMessage));
            if (available.size() < deliverNum) {
                throw new InsufficientException("区域内可用车辆数目小于订单要求");
            }
            int j=1;
            for (PooledIdAndEquipCache entry : available) {
                Long pooledId=entry.getPooledId();
                try {
                    Channel channel=idChannelPool.getChannel(pooledId);
                    //取出channel已经收到的任务列表
                    HashMap<Integer, Task> received=channel.attr(ScreenChannelInitializer.TASK_MAP).get();
                    if (received==null) {
                        received=new HashMap<>(16);
                        channel.attr(ScreenChannelInitializer.TASK_MAP).set(received);
                    }
                    int addNum=rate;
                    for (; addNum > 0 && j <= 25; j++) {
                        // 将任务加入对应 channel 的 received 列表中
                        Task task=createTask(taskMessage, j);
                        if (null==received.putIfAbsent(j, task)) {
                            addNum--;
                        }
                    }
                    log.info("已经往IMEI为:{}的channel中安排了{}个task", entry.getEquipment().getKey(), rate);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
//                    确保可以再次被其他任务调度
                    entry.releaseAllocate();
                }
            }
        }
    }

    /**
     * 创建设备分配任务信息
     *
     * @param taskMessage 支付成功信息
     * @param entryId     taskMap 中的唯一条目编号
     * @return 分配到每个设备的数据
     */
    public Task createTask(TaskMessage taskMessage, int entryId) {
        //组合广告内容
        String view=String.join(",", taskMessage.getProduceContext());
        Integer numPerEquip=taskMessage.getNumPerEquip();
        int rate=taskMessage.getRate();
        int numPerTask=numPerEquip/rate;
        return Task.builder()
                .oid(taskMessage.getOid())
                .entryId(entryId)
                .repeatNum(numPerTask)
                .sendIf(false)
                .uid(taskMessage.getUid())
                .view(view)
                .verticalView(taskMessage.getVertical())
                .build();

    }
}
