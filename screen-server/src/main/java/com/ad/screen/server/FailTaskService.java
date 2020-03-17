package com.ad.screen.server;

import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.TaskKey;

/**
 * @ClassName FailTaskService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 16:46
 * @Version V1.0
 **/
public interface FailTaskService {
    FailTask save(FailTask failTask);

    FailTask findByKey(TaskKey taskKey);
//    FailTask findByOidAndUid(Integer oid, Integer uid);

    void remove(TaskKey taskKey);
}
