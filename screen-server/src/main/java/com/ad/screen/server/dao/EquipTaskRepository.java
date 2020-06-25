package com.ad.screen.server.dao;

import com.ad.screen.server.entity.EquipTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public interface EquipTaskRepository extends JpaRepository<EquipTask, Integer> {


    @Query(nativeQuery = true, value = "UPDATE equip_task t set t.executed_num = t.executed_num + :executeNum where t.oid=:orderId and t.work_identity=:workIdentity")
    @Modifying()
    void executeNumInc(String workIdentity, int orderId, int executeNum);


    List<EquipTask> findEquipTasksByIdGreaterThanAndWorkIdentityAndExecutedIsFalseOrderById(int id, String currentId, Pageable pageable);

    @Query(nativeQuery = true, value = "update equip_task t set t.work_identity=:currentId where t.work_identity=:crashId and t.executed=false  and t.id >:crashRecord ")
    @Modifying
    Integer updateCrashWorkIdentify(@Param("crashId") String crashId,
                                    @Param("crashRecord") int crashRecord,
                                    @Param("currentId") String currentId);

    @Query(nativeQuery = true, value = "update equip_task t set t.executed = true where t.work_identity=:currentId and t.oid=:id and t.executed_num > t.total_num")
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    Integer tryTaskComplete(String currentId, int id);

}
