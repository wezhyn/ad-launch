package com.ad.admain.screen;

import com.ad.admain.screen.entity.FailTask;
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
    public FailTask findById(Integer id) {
        return failTaskRepository.findById(id).orElse(null);
    }
}
