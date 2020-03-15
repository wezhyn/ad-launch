package com.ad.screen.server.cache;

import com.ad.launch.order.AdEquipment;
import com.ad.launch.order.RemoteEquipmentServiceI;
import com.ad.launch.order.exception.NotEquipmentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName EquipmentCache
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/7 15:54
 * @Version V1.0
 **/
@Component("equipCache")
@Slf4j
public class EquipmentCacheService extends GuavaAbstractLoadingCache<String, AdEquipment> implements IEquipmentCache {
    @Resource
    private RemoteEquipmentServiceI equipmentService;




    @Override
    protected AdEquipment fetchData(String key) throws Exception {
        try {
            AdEquipment equipment=get(key);
            return equipment;
        } catch (Exception e) {
            log.debug("无法根据key{}获取到值,可能是数据库中不存在该编号", key);
            return null;
        }
    }

    @Override
    public AdEquipment get(String key) throws NotEquipmentException {
        return equipmentService.loadEquipByIemi(key);
    }
}
