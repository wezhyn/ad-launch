package com.ad.screen.server.service;

import com.ad.screen.server.entity.EquipTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wezhyn
 * @since 03.27.2020
 */
@Service
public class TestService {

    @Autowired
    private RedisTemplate<Integer, Integer> redisTemplate;

    @Autowired
    private EquipTaskService equipTaskService;

    @Transactional(rollbackFor=Exception.class)
    public void save() {
        redisTemplate.delete(100);
        equipTaskService.save(new EquipTask());
        throw new RuntimeException("test");
    }
}
