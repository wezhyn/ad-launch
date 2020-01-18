package com.ad.admain.controller.assignment;

import com.ad.admain.controller.assignment.entity.EquipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentStatusRepository extends JpaRepository<EquipmentStatus,Integer> {
}
