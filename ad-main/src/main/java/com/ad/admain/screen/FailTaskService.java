package com.ad.admain.screen;

import com.ad.admain.screen.entity.FailTask;

/**
 * @ClassName FailTaskService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 16:46
 * @Version V1.0
 **/
public interface FailTaskService {
    FailTask save(FailTask failTask);

    FailTask findById(Integer id);
}
