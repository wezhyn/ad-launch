package com.ad.screen.server.service;

import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.dao.EquipTaskRepository;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.TaskKey;
import com.ad.screen.server.exception.InsufficientException;
import com.wezhyn.project.AbstractBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
@Service
@Slf4j
public class EquipTaskServiceImpl extends AbstractBaseService<EquipTask, Integer> implements EquipTaskService {


    @Autowired
    private EquipTaskRepository equipTaskRepository;
    @Autowired
    private GlobalIdentify globalIdentify;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;


    @Override
    @Transactional(rollbackFor=Exception.class)
    public void mergeTaskExecStatistics(TaskKey key, int execute) {
        equipTaskRepository.executeNumInc(globalIdentify.getId(), key.getOid(), execute);
    }

    @Override
    public List<EquipTask> nextPreparedResume(int id, int limit) {
        return getRepository().findEquipTasksByIdGreaterThanAndWorkIdentityAndExecutedIsFalse(id,
                globalIdentify.getId(), PageRequest.of(0, limit));
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int transferCrashServer(String crashServer, int crashRecord) {
        return getRepository().updateCrashWorkIdentify(crashServer, crashRecord, globalIdentify.getId());
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void saveAndCheckOrder(EquipTask equipTask) {
        boolean isDump=false;
        try {
            getRepository().save(equipTask);
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
        }
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int checkTaskExecuted(int id) {
        return getRepository().updateEquipStatus(globalIdentify.getId(), id);
    }

    @Override
    public EquipTaskRepository getRepository() {
        return equipTaskRepository;
    }
}
