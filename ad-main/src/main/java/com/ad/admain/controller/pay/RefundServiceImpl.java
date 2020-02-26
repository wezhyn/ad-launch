package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.repository.RefundOrderRepository;
import com.ad.admain.controller.pay.to.RefundOrder;
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
public class RefundServiceImpl extends AbstractBaseService<RefundOrder,Integer> implements RefundService {

    @Autowired
    RefundOrderRepository refundOrderRepository;

    @Override
    public JpaRepository<RefundOrder, Integer> getRepository() {
        return refundOrderRepository;
    }
}
