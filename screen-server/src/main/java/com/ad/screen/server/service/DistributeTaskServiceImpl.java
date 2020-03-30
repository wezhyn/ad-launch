package com.ad.screen.server.service;

import com.ad.screen.server.cache.PooledIdAndEquipCache;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.event.DistributeTaskI;
import com.ad.screen.server.exception.InsufficientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wezhyn
 * @since 03.30.2020
 */
@Slf4j
@Service
public class DistributeTaskServiceImpl implements DistributeTaskService {

    @Autowired
    private EquipTaskService equipTaskService;
    @Autowired
    private PooledIdAndEquipCacheService pooledIdAndEquipCacheService;
    @Autowired
    private DistributeTaskI distributeTaskI;

    private final Object KEY_VALUE=new Object();
    private ConcurrentMap<TaskKey, Object> runningTask=new ConcurrentHashMap<>(128);

    @Override
    @Transactional(rollbackFor=Exception.class)
    public List<PooledIdAndEquipCache> saveAndCheckOrder(EquipTask equipTask) {
        boolean isDump=false;
        if (checkRunning(equipTask.getTaskKey())) {
            return Collections.emptyList();
        }
        try {
            equipTaskService.save(equipTask);
        } catch (Exception e) {
            log.error(e.getMessage());
            isDump=true;
        }
        if (!isDump) {
            int onlineNum=pooledIdAndEquipCacheService.count();
            Integer deliverNum=equipTask.getDeliverNum();
            //目前没有这么多的在线车辆数,退出
            if (onlineNum < deliverNum) {
                throw new InsufficientException("目前没有这么多的在线车辆数");
            }
            //目前区域内可用符合订单要求的车辆数小于订单要求投放的车辆数，退出
            List<PooledIdAndEquipCache> available=distributeTaskI.availableEquips(equipTask);
            if (available.size() < deliverNum) {
                throw new InsufficientException("区域内可用车辆数目小于订单要求");
            }
            return available;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean checkRunningAndPut(TaskKey key) {
        final Object absent=runningTask.putIfAbsent(key, KEY_VALUE);
        return absent==null;
    }

    @Override
    public boolean checkRunning(TaskKey key) {
        return runningTask.containsKey(key);
    }

    @Override
    public void remove(TaskKey key) {
        runningTask.remove(key);
    }
}
