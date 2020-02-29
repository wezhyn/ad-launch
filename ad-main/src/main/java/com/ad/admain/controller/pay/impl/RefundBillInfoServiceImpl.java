package com.ad.admain.controller.pay.impl;

import com.ad.admain.controller.pay.RefundBillInfoService;
import com.ad.admain.controller.pay.repository.RefundBillInfoRepository;
import com.ad.admain.controller.pay.to.RefundBillInfo;
import com.wezhyn.project.AbstractBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @ClassName RefundServiceImpl
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/25 13:16
 * @Version 1.0
 */
@Service
public class RefundBillInfoServiceImpl extends AbstractBaseService<RefundBillInfo, Integer> implements RefundBillInfoService {

    @Autowired
    RefundBillInfoRepository refundOrderRepository;

    @Override
    public JpaRepository<RefundBillInfo, Integer> getRepository() {
        return refundOrderRepository;
    }
}
