package com.ad.screen.server.handler;

import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.FixedTask;
import com.ad.screen.server.entity.MemoryCompletion;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.service.CompletionService;
import com.ad.screen.server.vo.req.CompleteNotificationMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @ClassName CompleteMsgHandler
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:07
 * @Version V1.0
 **/
@Component
@Slf4j
@ChannelHandler.Sharable
public class CompleteMsgHandler extends BaseMsgHandler<CompleteNotificationMsg> {
    @Autowired
    private CompletionService memoryCompletionService;

    @Autowired
    PooledIdAndEquipCacheService cacheService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CompleteNotificationMsg msg) throws Exception {
        log.warn("收到了imei号为{}的第{}个条目编号的完成消息", msg.getEquipmentName(), msg.getNetData());
        HashMap<Integer, Task> received=ctx.channel().attr(ScreenChannelInitializer.TASK_MAP).get();
//        条目编号
        Integer entryId=msg.getNetData();
        Task task=received.get(entryId);
        String iemi=msg.getEquipmentName();
        final FixedTask preTask=task.getPreTask();
        if (preTask==null) {
            log.error("重复通知：{} at {}", msg, LocalDateTime.now());
            return;
        }
        try {
            MemoryCompletion com=MemoryCompletion.builder()
                    .adOrderId(task.getOrderId())
                    .executedNum(preTask.getRepeatNum())
                    .driverId(task.getDeliverUserId())
                    .build();
            memoryCompletionService.completeIncrMemory(com);
            if (task.getRepeatNum()==0) {
                received.remove(task.getEntryId());
                boolean equipTaskComplete=true;
                for (Task remainTask : received.values()) {
                    if (remainTask.getOrderId().equals(task.getOrderId())) {
                        equipTaskComplete=false;
                        break;
                    }
                }
                if (equipTaskComplete) {
//                        当前Task 帧完成, 删除 redis 中的当前帧数据，保存到数据库中
                    memoryCompletionService.memoryToDisk(task.getOrderId(), task.getDeliverUserId());
                    cacheService.get(iemi).restIncr(1);
                }
            } else {
                task.setPreTask(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
