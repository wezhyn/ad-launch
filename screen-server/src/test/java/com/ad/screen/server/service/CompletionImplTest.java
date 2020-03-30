package com.ad.screen.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CompletionImplTest {


    @Autowired
    private RedisTemplate<Integer, Integer> redisTemplate;

    @Test
    public void delete() {
        final BoundHashOperations<Integer, Integer, Integer> boundHashOps=redisTemplate.boundHashOps(2);
        final Integer integer=boundHashOps.get(1011);

        System.out.println(integer);
    }

    @Test
    public void testDelete() {
        final BoundHashOperations<Integer, Integer, Integer> bound=redisTemplate.boundHashOps(100);
        final Long increment=bound.increment(12, 10);
        System.out.println(increment);
        System.out.println(bound.get(12));

    }

}