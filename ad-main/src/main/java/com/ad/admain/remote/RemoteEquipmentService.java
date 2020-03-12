package com.ad.admain.remote;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.remote.convert.RemoteEquipmentMapper;
import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.launch.order.exception.NotEquipmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Component(value="remoteEquipmentService")
public class RemoteEquipmentService implements RemoteEquipmentServiceI {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private RemoteEquipmentMapper equipmentMapper;

    @Override
    public AdEquipment loadEquipByIemi(String iemi) throws NotEquipmentException {
        final Equipment equipment=equipmentService.findEquipmentByIMEI(iemi);
        if (equipment==null) {
            throw new NotEquipmentException("无当前设备iemi信息");
        }
        return equipmentMapper.toDto(equipment);
    }

    @Override
    public AdEquipment mergeEquip(AdEquipment equipment) {
        final Equipment updated=equipmentService.update(equipmentMapper.toTo(equipment));
        return equipmentMapper.toDto(updated);
    }
}
