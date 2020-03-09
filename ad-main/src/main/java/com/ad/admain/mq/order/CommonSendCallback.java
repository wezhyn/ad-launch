package com.ad.admain.mq.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

/**
 * @ClassName CommonSendCallback
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 15:02
 * @Version V1.0
 **/
@Slf4j
public class CommonSendCallback<T> implements SendCallback {
    private final T message;

    public CommonSendCallback(T message) {
        this.message=message;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        log.debug("send cancel order : {} success", sendResult.getMsgId());
    }

    @Override
    public void onException(Throwable e) {
        // todo: 保存失败信息，补偿机制
        log.error("send message error: {}", message);
    }
}
