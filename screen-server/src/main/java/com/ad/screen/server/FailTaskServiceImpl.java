package com.ad.screen.server;

import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.TaskKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName FailTaskServiceImpl
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 16:47
 * @Version V1.0
 **/
@Service
public class FailTaskServiceImpl implements FailTaskService {
    @Autowired
    FailTaskRepository failTaskRepository;

    @Override
    public FailTask save(FailTask failTask) {
        return failTaskRepository.save(failTask);
    
    }
    

    @Override
    public FailTask findByKey(TaskKey taskKey) {
        return failTaskRepository.findById(taskKey).orElse(null);
    }

    @Override
    public void remove(TaskKey taskKey) {
        failTaskRepository.deleteById(taskKey);
    }


}
