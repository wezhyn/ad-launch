package com.ad.screen.server.mq;

import com.ad.launch.order.TaskMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
@Service
public class RedisPayMessageManager implements PayMessageManager {


    @Autowired
    @Qualifier(value="IntegerRedisTemplate")
    private RedisTemplate<Integer, Integer> redisTemplate;


    @Override
    public boolean isDuplicate(TaskMessage taskMessage) {
        final BoundValueOperations<Integer, Integer> oidBound=redisTemplate.boundValueOps(taskMessage.getOid());
        final Long increment=oidBound.increment(1);
        oidBound.expire(1, TimeUnit.HOURS);
        return !Long.valueOf(1).equals(increment);
    }

    @Override
    public Long getTaskMessageCertificate(TaskMessage taskMessage) {
        final BoundValueOperations<Integer, Integer> oidBound=redisTemplate.boundValueOps(taskMessage.getOid());
        final Long increment=oidBound.increment(1);
        oidBound.expire(10, TimeUnit.MINUTES);
        return increment;
    }
}
