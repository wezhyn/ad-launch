package com.ad.screen.server.handler;

import com.ad.launch.order.AdRemoteOrder;
import com.ad.launch.order.RemoteAdOrderServiceI;
import com.ad.screen.server.CompletionI;
import com.ad.screen.server.cache.EquipmentCacheService;
import com.ad.screen.server.entity.Completion;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.server.ScreenChannelInitializer;
import com.ad.screen.server.vo.req.CompleteNotificationMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    @Resource
    RemoteAdOrderServiceI remoteAdOrderServiceI;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CompleteNotificationMsg msg) throws Exception {
        log.warn("收到了imei号为{}的第{}项任务完成的消息",msg.getEquipmentName(),msg.getNetData());
        Attribute<List<Task>> attr = ctx.channel().attr(ScreenChannelInitializer.TASK_LIST);
        List<Task> received = attr.get();
        Integer id=msg.getNetData();
        Task task=received.get(id - 1);
        if (task.getOid()==0) {
            log.debug("该条目为空白帧");
        } else {
            try {
                String imei=msg.getEquipmentName();
                Completion completion=completionI.findByOidAndUid(task.getOid(), task.getUid());
                if (completion==null) {
                    Completion com=Completion.builder()
                            .adOrderId(task.getOid())
                            .executedTimes(task.getRepeatNum())
                            .isPaid(false)
                            .uid(task.getUid())
                            .build();
                    completionI.save(com);
                    log.info("完成了条目编号为{}的小任务，持久化完成",id);
                } else {

                    completion.setExecutedTimes(completion.getExecutedTimes() + task.getRepeatNum());
                    completionI.save(completion);
                    log.debug("完成了条目编号为{}的小任务 更新执行次数",id+1);


                }
                //完成任务后删除该index的任务并更新attr属性
                received.remove(id-1);
                attr.set(received);
                AdRemoteOrder adRemoteOrder = remoteAdOrderServiceI.findByOid(task.getOid());
                Double executed = adRemoteOrder.getExecuted();
                if (executed == null) {
                    executed = Double.valueOf(task.getRepeatNum());
                }else {
                    executed+= Double.valueOf(task.getRepeatNum());
                }

                adRemoteOrder.setExecuted(executed);
                remoteAdOrderServiceI.save(adRemoteOrder);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
