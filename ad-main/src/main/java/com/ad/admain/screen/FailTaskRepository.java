package com.ad.admain.screen;

import com.ad.admain.screen.entity.FailTask;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName TaskFailRepository
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/9 16:45
 * @Version V1.0
 **/
public interface FailTaskRepository extends JpaRepository<FailTask,Integer> {

}
