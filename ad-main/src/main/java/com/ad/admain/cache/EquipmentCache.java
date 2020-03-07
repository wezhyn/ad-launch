package com.ad.admain.cache;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 15:54
 * @Version V1.0
 **/
@Component
@Slf4j
public class EquipmentCache extends GuavaAbstractLoadingCache<String, Equipment> implements IlocalCache<String,Equipment>{
    private EquipmentService equipmentService;

    public EquipmentCache(EquipmentService equipmentService){
        this.equipmentService = equipmentService;
    }


    @Override
    protected Equipment fetchData(String key) throws Exception {
        try {
            Equipment equipment =  get(key);
            return equipment;
        } catch (Exception e) {
            log.debug("无法根据key{}获取到值,可能是数据库中不存在该编号",key);
            return null;
        }
    }

    @Override
    public Equipment get(String key) {
        return equipmentService.findEquipmentByIMEI(key);
    }
}