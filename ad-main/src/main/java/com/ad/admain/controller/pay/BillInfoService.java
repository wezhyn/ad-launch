package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.to.AdBillInfo;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.PayType;
import com.wezhyn.project.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author wezhyn
 * @since 12.01.2019
 */
public interface BillInfoService extends BaseService<AdBillInfo, Integer> {


    /**
     * 创建订单信息
     *
     * @param order   order
     * @param payType 选择支付的类型
     * @return orderInfo
     */
    AdBillInfo getOrCreateOrder(AdOrder order, PayType payType);


    Optional<AdBillInfo> getByOrderId(Integer id);


    /**
     * 根据查找类型进行 BillInfo 筛选
     *
     * @param type    type
     * @param context 内容
     * @return orderList
     */
    Page<AdBillInfo> search(BillInfoSearchType type, String context, Pageable pageable);
}
