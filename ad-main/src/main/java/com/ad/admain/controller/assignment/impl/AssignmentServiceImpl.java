package com.ad.admain.controller.assignment.impl;

import com.ad.admain.controller.assignment.AssignmentRepository;
import com.ad.admain.controller.assignment.AssignmentService;
import com.ad.admain.controller.assignment.entity.Assignment;
import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.controller.pay.repository.OrderReposity;
import com.ad.admain.controller.pay.to.Order;
import com.ad.admain.controller.pay.to.Value;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName DistributeServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/1/16 13:11
 * @Version 1.0
 */
@Service
public class AssignmentServiceImpl extends AbstractBaseService<Assignment, Integer> implements AssignmentService {
    private static final int adNumsPerCar = 20;//每辆车每5分钟可以处理的广告条数
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    OrderReposity orderReposity;
    @Autowired
    EquipmentService equipmentService;
    @Override
    public JpaRepository<Assignment, Integer> getRepository() {
        return assignmentRepository;
    }
    /**
    * @description  如果订单要求的分布到车上的数量大于目标区域范围内可用车的数量或任务分配失败则返回false,否则返回true
    * @author zlb
    * @updateTime 2020/1/18 10:40
    * @Return false任务分配失败,true表示任务分配成功
    */
    @Override
    public Boolean distributeTasks(Order order) {
        List<Assignment> assignmentList = new ArrayList<>();
        String content = "";
        Integer nums = order.getNum();
        Integer assignRate= order.getRate();
        Long assignEquipNums =order.getDeliverNum();
        Long assginNumsPerEquip = nums/assignEquipNums;
        // 查找出目标投放范围内的车辆数目
        Long onlineEquips = equipmentService.countByStatusAndRegion(order.getRate(),1,order.getLongitude(),order.getLatitude(),order.getScope()); //查找订单的区域内的车辆数
        if (onlineEquips<order.getDeliverNum()){
            return false;
        }
        try {
            //获取订单内中的广告内容
            List<Value> valueList = order.getValueList();
            //分配任务数目计算
            Integer carNums = Math.toIntExact(order.getNum() / (adNumsPerCar * order.getDeliverNum()));
            //查找出可用目标车辆
            List<Equipment> equipmentList = equipmentService.findAllAvailableEquips(order.getRate(),1,order.getLongitude(),order.getLatitude(),order.getScope(), carNums);
            Iterator<Value> iterator = valueList.iterator();
            while (iterator.hasNext()){
                Value value = iterator.next();
                if (iterator.hasNext()){
                    content = content+value.getVal()+'#';
                }
                else
                    content +=value.getVal();
            }
            Assignment assignment = new Assignment();
            assignment.setContent(content);
            assignment.setNum(assginNumsPerEquip);
            assignment.setOrder(order);
            assignment.setStatus(false);
            assignment.setTriggerTime(order.getStartTime());

            for (Equipment equipment :equipmentList) {
               assignment.setEquipment(equipment);
               assignmentRepository.save(assignment);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    return false;
    }
}
