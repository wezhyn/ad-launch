package com.ad.screen.server.dao;

import com.ad.screen.server.entity.EquipTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public interface EquipTaskRepository extends JpaRepository<EquipTask, Integer> {


    @Query(nativeQuery=true, value="UPDATE equip_task t set t.executed_num = t.executed_num + :executeNum where t.oid=:orderId and t.work_identity=:workIdentity")
    @Modifying()
    void executeNumInc(String workIdentity, int orderId, int executeNum);


    List<EquipTask> findEquipTasksByIdGreaterThan(int id, Pageable pageable);
}
