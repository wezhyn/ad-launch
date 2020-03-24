package com.ad.screen.server;

import com.ad.launch.order.AdRemoteOrder;
import com.ad.launch.order.RemoteAdOrderServiceI;
import com.ad.launch.order.TaskMessage;
import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.Task;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.mq.CommonSendCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName InitialService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/19 1:05
 * @Version V1.0
 **/
@Component
@Slf4j
public class InitialService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    FailTaskService failTaskService;
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Resource
    RemoteAdOrderServiceI adOrderServiceI;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<FailTask> failTasks = failTaskService.findAll();
        if (failTasks!=null && failTasks.size()!=0){
            for (FailTask failTask : failTasks
            ) {
                rocketMQTemplate.syncSend("fail_task_topic",failTask);
                log.info("已发送失败任务");
            }

        }
        afterDownTime();
    }



    public void afterDownTime(){
           List<AdRemoteOrder> adRemoteOrders =  adOrderServiceI.findByEnum(3);
            if (adRemoteOrders == null) {
                return;
            }
            for (AdRemoteOrder adRemoteOrder: adRemoteOrders
                 ) {
               Integer oldDeliverNum =  adRemoteOrder.getDeliverNum();
               Integer executed = adRemoteOrder.getExecuted();
               Integer total = adRemoteOrder.getNum();
               Integer numPerEquip = total/oldDeliverNum;
                //余下的未执行的量
                Integer left = total-executed;
               Double longitude = adRemoteOrder.getLongitude();
               Double latitude =adRemoteOrder.getLatitude();
               Double scope = adRemoteOrder.getScope();
               List<String> viewList = adRemoteOrder.getProduceContext();
               Boolean vertical = adRemoteOrder.getProduce().getVertical();
               Integer oid =  adRemoteOrder.getId();
               Integer rate = adRemoteOrder.getRate();
               Integer uid = adRemoteOrder.getUid();
                StringBuilder sb = new StringBuilder();
                for (String str :viewList){
                    sb.append(str);
                }
                //当情况为未执行量已经小于投放到每辆车上的数量 是做一个failTask发出且频率视作为repeatNum
                if (numPerEquip>=left){
                    TaskKey taskKey = new TaskKey(oid,uid);
                    FailTask failTask = FailTask.builder()
                            .id(taskKey)
                            .longitude(longitude)
                            .latitude(latitude)
                            .scope(scope)
                            .rate(1)
                            .repeatNum(left)
                            .view(sb.toString())
                            .verticalView(vertical)
                            .build();
                    rocketMQTemplate.asyncSend("fail_task_topic",failTask,new CommonSendCallback<>(failTask));
                }
                //当前情况为
                else {
                    int deliverNum = left%numPerEquip ==0 ? left/numPerEquip : left/numPerEquip+1;
                    TaskMessage taskMessage = TaskMessage.builder()
                            .deliverNum(deliverNum)
                            .latitude(latitude)
                            .longitude(longitude)
                            .numPerEquip(numPerEquip)
                            .oid(oid)
                            .rate(rate)
                            .totalNum(numPerEquip*deliverNum)
                            .scope(scope)
                            .vertical(vertical)
                            .produceContext(viewList)
                            .uid(uid)
                            .build();
                    rocketMQTemplate.asyncSend("task_message_topic",taskMessage,new CommonSendCallback<>(taskMessage));
                }

            }
    }
}
