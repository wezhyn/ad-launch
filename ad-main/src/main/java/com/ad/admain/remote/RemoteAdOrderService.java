package com.ad.admain.remote;

import com.ad.admain.controller.pay.AdOrderService;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.remote.convert.RemoteOrderMapper;
import com.ad.launch.order.AdRemoteOrder;
import com.ad.launch.order.OrderStatus;
import com.ad.launch.order.RemoteAdOrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName RemoteAdOrderService
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/17 19:28
 * @Version V1.0
 **/
@Component("remoteAdOrderService")
public class RemoteAdOrderService implements RemoteAdOrderServiceI {
    @Autowired
    RemoteOrderMapper remoteOrderMapper;
    @Autowired
    AdOrderService adOrderService;
    @Override
    public void save(AdRemoteOrder adRemoteOrder) {
         adOrderService.update(remoteOrderMapper.toTo(adRemoteOrder));
    }

    @Override
    public AdRemoteOrder findByOid(Integer id) {
        com.ad.admain.controller.pay.to.AdOrder adOrder =  adOrderService.findById(id);
        if (adOrder==null){
            return null;
        }
        else
            return remoteOrderMapper.toDto(adOrder);
    }

    @Override
    public Integer updateExecuted(AdRemoteOrder adRemoteOrder) {
        return adOrderService.updateExecuted(adRemoteOrder.getId(),adRemoteOrder.getExecuted());
    }

    @Override
    public List<AdRemoteOrder> findByEnum(Integer type) {
        List<AdOrder> adOrders = adOrderService.findByEnum(type);
        if (adOrders == null||adOrders.size()==0) {
            return null;
        }else {
            return remoteOrderMapper.toDtoList(adOrders);
        }
    }


}
