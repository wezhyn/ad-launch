package com.ad.screen.server.service;

import com.ad.screen.server.config.GlobalIdentify;
import com.ad.screen.server.dao.EquipTaskRepository;
import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.TaskKey;
import com.wezhyn.project.AbstractBaseService;
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
public class EquipTaskServiceImpl extends AbstractBaseService<EquipTask, Integer> implements EquipTaskService {


    @Autowired
    private EquipTaskRepository equipTaskRepository;
    @Autowired
    private GlobalIdentify globalIdentify;


    @Override
    @Transactional(rollbackFor=Exception.class)
    public void mergeTaskExecStatistics(TaskKey key, int execute) {
        equipTaskRepository.executeNumInc(globalIdentify.getId(), key.getOid(), execute);
    }

    @Override
    public List<EquipTask> nextPreparedResume(int id, int limit) {
        return getRepository().findEquipTasksByIdGreaterThan(id, PageRequest.of(0, limit));
    }

    @Override
    public EquipTaskRepository getRepository() {
        return equipTaskRepository;
    }
}
