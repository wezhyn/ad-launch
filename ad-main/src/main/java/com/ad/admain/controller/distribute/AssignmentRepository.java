package com.ad.admain.controller.distribute;

import com.ad.admain.controller.distribute.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment,Integer> {
}
