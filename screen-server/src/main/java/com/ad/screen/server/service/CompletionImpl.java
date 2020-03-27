package com.ad.screen.server.service;

import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.dao.DiskCompletionRepository;
import com.ad.screen.server.dao.EquipTaskRepository;
import com.ad.screen.server.entity.DiskCompletion;
import com.ad.screen.server.entity.MemoryCompletion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @ClassName CompletionImpl
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/8 20:49
 * @Version V1.0
 **/
@Service
public class CompletionImpl implements CompletionService {

    @Autowired
    private RedisTemplate<Integer, Integer> redisTemplate;

    @Autowired
    private DiskCompletionRepository diskCompletionRepository;
    @Autowired
    private EquipTaskRepository equipTaskRepository;
    @Autowired
    private GlobalIdentify globalIdentify;

    @Override
    public void completeIncrMemory(MemoryCompletion completion) {
        final BoundHashOperations<Integer, Object, Object> boundHashOps=redisTemplate.boundHashOps(completion.getAdOrderId());
        boundHashOps.increment(completion.getDriverId(), completion.getExecutedNum());
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer forOrderTotal(Integer orderId) {
        final Map<Object, Object> entries=redisTemplate.opsForHash().entries(orderId);
        int totalNul=0;
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Integer driverId=(Integer) entry.getKey();
            Integer driverExe=(Integer) entry.getValue();
            diskCompletionRepository.save(new DiskCompletion(driverExe, orderId, driverId));
            totalNul+=driverExe;
        }
        if (totalNul > 0) {
            equipTaskRepository.executeNumInc(globalIdentify.getId(), orderId, totalNul);
            redisTemplate.delete(orderId);
        }
        return totalNul;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer memoryToDisk(Integer orderId, Integer driverId) {
        final BoundHashOperations<Integer, Integer, Integer> boundHashOps=redisTemplate.boundHashOps(orderId);
        Integer exec=boundHashOps.get(driverId);
        if (exec!=null) {
            boundHashOps.delete(driverId);
            equipTaskRepository.executeNumInc(globalIdentify.getId(), orderId, exec);
            diskCompletionRepository.save(new DiskCompletion(exec, orderId, driverId));
        }
        return exec;
    }
}
