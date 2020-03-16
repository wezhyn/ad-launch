package screen;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName TaskProduceImplTest
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 11:18
 * @Version V1.0
 **/
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class TaskProduceImplTest {
//    @Autowired
//    RocketMQTemplate rocketMQTemplate;
//
//    @Test
//    public void testSend() {
//        FailTask failTask=FailTask.builder()
//                .orderId(1)
//                .uid(2)
//                .num(100)
//                .build();
//        rocketMQTemplate.asyncSend("fail-task", failTask, new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//                log.debug("发送成功");
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                log.debug("发送失败");
//            }
//        });
//    }

    @Test
    public void sendTask() {
    }

    @Test
    public void countOnlineNums() {
    }

    @Test
    public void freeEquips() {
    }

    @Test
    public void scopeFreeEquips() {
    }

    @Test
    public void scopeAvailableFreeEquips() {
    }
}