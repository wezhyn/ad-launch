package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author : wezhyn
 * @date : 2020/2/24
 */
@Repository
public interface AdOrderRepository extends JpaRepository<AdOrder, Integer> {


    /**
     * 更新订单属性
     *
     * @param orderId      订单id
     * @param originStatus 原状态
     * @param nextStatus   下一个状态
     * @return 1
     */
    @Modifying
    @Query("update ad_order o set o.orderStatus=:nextStatus where o.id=:orderId and o.orderStatus=:originStatus")
    Integer updateOrderStatus(Integer orderId, OrderStatus originStatus, OrderStatus nextStatus);
}
