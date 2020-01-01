package com.ad.admain.controller.pay;

import com.ad.admain.controller.pay.to.Order;
import com.wezhyn.project.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : wezhyn
 * @date : 2019/12/26
 */
public interface OrderService extends BaseService<Order, Integer> {

    /**
     * 根据查找类型进行 Order 筛选
     *
     * @param type    type
     * @param context 内容
     * @return orderList
     */
    Page<Order> search(OrderSearchType type, String context, Pageable pageable);


}
