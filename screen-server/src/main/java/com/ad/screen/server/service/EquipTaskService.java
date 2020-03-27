package com.ad.screen.server.service;

import com.ad.screen.server.entity.EquipTask;
import com.ad.screen.server.entity.TaskKey;
import com.wezhyn.project.BaseService;

import java.util.List;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public interface EquipTaskService extends BaseService<EquipTask, Integer> {

    /**
     * 更新任务的执行次数
     *
     * @param key     key
     * @param execute 执行次数
     */
    void mergeTaskExecStatistics(TaskKey key, int execute);

    /**
     * 查找下一个代恢复订单
     *
     * @param id    id
     * @param limit limit
     * @return equipTask
     */
    List<EquipTask> nextPreparedResume(int id, int limit);

}
