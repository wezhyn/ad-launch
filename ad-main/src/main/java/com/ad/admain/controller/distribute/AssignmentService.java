package com.ad.admain.controller.distribute;

import com.ad.admain.controller.distribute.entity.Assignment;
import com.ad.admain.controller.pay.to.Order;
import com.wezhyn.project.BaseService;

public interface AssignmentService extends BaseService<Assignment,Integer> {
    Boolean distributeTasks(Order order);
}
