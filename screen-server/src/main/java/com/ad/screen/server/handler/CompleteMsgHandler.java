package com.ad.screen.server.handler;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
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
        final PooledIdAndEquipCache equipCache=ctx.channel().attr(ScreenChannelInitializer.POOLED_EQUIP_CACHE).get();
//        条目编号
        Integer entryId=msg.getNetData();
        int taskEntryId=entryId%ScreenChannelInitializer.SCHEDULE_NUM;
        try {
            Task task=equipCache.getTask(taskEntryId);
            String iemi=msg.getEquipmentName();
            final FixedTask preTask=task.getPreTask();
            if (preTask==null || entryId!=preTask.getEquipEntryId()) {
                log.debug("重复通知：{} at {}", msg, LocalDateTime.now());
                return;
            } else {
                log.info("收到了imei号为{}的第{}个条目编号的完成消息", msg.getEquipmentName(), msg.getNetData());
                task.setPreTask(null);
            }
            MemoryCompletion com=createMemoryRecord(preTask);
            memoryCompletionService.completeIncrMemory(com);
            if (task.getRepeatNum()==0) {
//              当前Task 帧完成, 删除 redis 中的当前帧数据，保存到数据库中
                equipCache.completeTask(taskEntryId);
                memoryCompletionService.memoryToDisk(task.getOrderId(), task.getDeliverUserId());
                cacheService.get(iemi).restIncr(task.getRate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MemoryCompletion createMemoryRecord(FixedTask preTask) {
        return MemoryCompletion.builder()
                .adOrderId(preTask.getTask().getOrderId())
                .executedNum(preTask.getRepeatNum())
                .driverId(preTask.getTask().getDeliverUserId())
                .build();
    }
}
