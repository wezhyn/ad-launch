package com.ad.admain.screen.service.impl;

import com.ad.admain.screen.dao.ServerInfoRepository;
import com.ad.admain.screen.entity.ServerInfo;
import com.ad.admain.screen.service.ServerInfoService;
import com.wezhyn.project.AbstractBaseService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * @ClassName ServerInfoServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/23 2:24
 * @Version 1.0
 */
@Service
public class ServerInfoServiceImpl extends AbstractBaseService<ServerInfo,Integer> implements ServerInfoService {

    @Autowired
    ServerInfoRepository serverInfoRepository;


    @Override
    public JpaRepository<ServerInfo, Integer> getRepository() {
        return serverInfoRepository;
    }
}
