package com.ad.admain.controller.distribute.impl;

import com.ad.admain.controller.distribute.DistributeService;
import com.ad.admain.controller.distribute.Task;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName DistributeServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/16 13:11
 * @Version 1.0
 */
public class DistributeServiceImpl extends AbstractBaseService<Task, Integer> implements DistributeService {
    @Override
    public JpaRepository<Task, Integer> getRepository() {
        return null;
    }
}
