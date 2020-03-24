package com.ad.launch.order;

import java.util.List;

/**
 * @ClassName RemoteAdOrderServiceI
 * @Description
 * @Author ZLB_KAM
 * @Date 2020/3/17 19:30
 * @Version V1.0
 **/
public interface RemoteAdOrderServiceI {
    void save(AdRemoteOrder adRemoteOrder);

    AdRemoteOrder findByOid(Integer id);

    Integer updateExecuted(AdRemoteOrder adRemoteOrder);

    List<AdRemoteOrder> findByEnum(Integer type);


}
