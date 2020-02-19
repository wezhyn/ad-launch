package com.ad.admain.controller.quartz.dao;

import com.ad.admain.controller.quartz.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName JobEntityRepository
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/26 11:31
 * @Version 1.0
 */
public interface JobEntityRepository extends JpaRepository<JobEntity,Integer>  {
    JobEntity getById(Integer id);

    List<JobEntity> findAllByStatusEquals(String status);

}
