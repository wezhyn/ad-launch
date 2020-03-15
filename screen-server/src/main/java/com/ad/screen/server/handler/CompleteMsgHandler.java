package com.ad.screen.server.handler;

import com.ad.screen.server.CompletionI;
import com.ad.screen.server.cache.EquipmentCacheService;
import com.ad.screen.server.entity.Completion;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.vo.req.CompleteNotificationMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
    CompletionI completionI;
    @Autowired
    EquipmentCacheService equipmentCacheService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CompleteNotificationMsg msg) throws Exception {
        List<Task> tasks=ctx.channel().attr(ScreenChannelInitializer.TASK_LIST).get();

        String id=msg.getNetData();
        Task task=tasks.get(Integer.parseInt(id) - 1);
        if (task.getAdOrderId()==0) {
            log.debug("该条目为空白帧");
        } else {
            String imei=msg.getEquipmentName();
            Completion completion=completionI.findByOidAndUid(task.getAdOrderId(), task.getUid());
            if (completion==null) {
                Completion com=Completion.builder()
                        .adOrderId(task.getAdOrderId())
                        .executedTimes(task.getRepeatNum())
                        .isPaid(false)
                        .uid(task.getUid())
                        .build();
                completionI.save(com);
                log.debug("完成task:{}", id);
            } else {
                completion.setExecutedTimes(completion.getExecutedTimes() + task.getRepeatNum());
                completionI.save(completion);
            }
        }
    }
}
