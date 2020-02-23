package com.ad.admain.screen.service;

import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.screen.entity.RemoteInfo;
import com.wezhyn.project.BaseService;

/**
 * @ClassName ScreenService
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 2:19
 * @Version 1.0
 */
public interface RemoteInfoService extends BaseService<RemoteInfo,Integer> {
    RemoteInfo findByEquipId(int equipId);
}
