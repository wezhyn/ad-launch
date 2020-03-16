package com.ad.screen.server;

import com.ad.screen.server.entity.FailTask;
import com.ad.screen.server.entity.TaskKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName TaskFailRepository
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 16:45
 * @Version V1.0
 **/
public interface FailTaskRepository extends JpaRepository<FailTask, TaskKey> {
}
