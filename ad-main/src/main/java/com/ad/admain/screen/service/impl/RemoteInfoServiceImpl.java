package com.ad.admain.screen.service.impl;

import com.ad.admain.screen.dao.RemoteInfoRepository;
import com.ad.admain.screen.entity.RemoteInfo;
import com.ad.admain.screen.service.RemoteInfoService;
import com.sun.org.apache.regexp.internal.RE;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @ClassName RemoteInfoImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 2:24
 * @Version 1.0
 */
@Service
public class RemoteInfoServiceImpl extends AbstractBaseService<RemoteInfo,Integer> implements RemoteInfoService {

    @Autowired
    RemoteInfoRepository remoteInfoRepository;


    @Override
    public JpaRepository<RemoteInfo, Integer> getRepository() {
        return remoteInfoRepository;
    }

    @Override
    public RemoteInfo findByEquipId(int equipId) {
        return remoteInfoRepository.findByEquipment_Id(equipId);
    }

    @Override
    public RemoteInfo findByIpAndPort(String ip, int port) {
        return remoteInfoRepository.findByIpEqualsAndPortEquals(ip,port);
    }
}
