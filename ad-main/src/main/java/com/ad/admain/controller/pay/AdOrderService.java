package com.ad.admain.controller.pay;

import java.util.List;
import java.util.Optional;

import com.ad.admain.controller.pay.dto.ITopUserNum;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import com.wezhyn.project.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : wezhyn
 * @date : 2019/12/26
 */
public interface AdOrderService extends BaseService<AdOrder, Integer> {

    /**
     * 根据查找类型进行 Order 筛选
     *
     * @param type    type
     * @param context 内容
     * @return orderList
     */
    Page<AdOrder> search(OrderSearchType type, String context, Pageable pageable);

    Boolean isUserOrder(Integer orderId, Integer UserId);

    List<ITopUserNum> topAd();

    /**
     * 查找某个用户的订单
     *
     * @param orderId id
     * @param userId  uId
     * @return order
     */
    Optional<AdOrder> findUserOrder(Integer orderId, Integer userId);

    /**
     * 展示全部list
     *
     * @return list
     */
    Page<AdOrder> listOrdersWithUsername(int limit, int page);

    int verifyOrder(AdOrder order);

    Optional<AdOrder> trySuccessOrder(Integer orderId, Integer uid);

    Page<AdOrder> listUserOrders(Integer userId, Pageable pageable);

    @Transactional(readOnly = true)
    Double sumUserOrders(Integer userId);

    boolean modifyOrderStatus(Integer orderId, OrderStatus orderStatus);

    /**
     * 退款失败回退到原来的订单状态
     *
     * @param orderId      orderId
     * @param originStatus 原状态
     * @return 是否修改成功
     */
    boolean rollbackRefundOrderStatus(Integer orderId, OrderStatus originStatus);

    AdOrder findById(Integer id);

    Integer updateExecuted(Integer oid, Integer executed);

    List<AdOrder> findByEnum(Integer type);
}
