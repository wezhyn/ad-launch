package com.ad.admain.screen.dao;

import com.ad.admain.screen.entity.RemoteInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemoteInfoRepository extends JpaRepository<RemoteInfo,Integer> {
    RemoteInfo findByEquipment_Id(int equipId);
    RemoteInfo findByIpEqualsAndPortEquals(String ip,int port);
}
