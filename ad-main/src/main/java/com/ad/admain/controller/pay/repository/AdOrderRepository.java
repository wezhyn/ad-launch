package com.ad.admain.controller.pay.repository;

import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

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
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("update ad_order o set o.orderStatus=:nextStatus where o.id=:orderId and o.orderStatus=:originStatus")
    Integer updateOrderStatus(Integer orderId, OrderStatus originStatus, OrderStatus nextStatus);

    /**
     * 更新订单属性
     *
     * @param orderId      订单id
     * @param originStatus 原状态
     * @param nextStatus   下一个状态
     * @return 1
     */
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    @Query("update ad_order o set o.orderStatus=:nextStatus where o.id=:orderId and o.uid=:uid and o.orderStatus=:originStatus")
    Integer updateOrderStatus(Integer orderId, Integer uid, OrderStatus originStatus, OrderStatus nextStatus);

    /**
     * 检查订单是否存在
     *
     * @param id  id
     * @param uid uid
     * @return true
     */
    Boolean existsByIdAndUid(Integer id, Integer uid);

    /**
     * 查找某个用户的最近订单列表
     *
     * @param uId      userId
     * @param pageable page
     * @return page
     */
    Page<AdOrder> findAdOrdersByUidAndIsDeleteIsFalse(Integer uId, Pageable pageable);


    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query("update ad_order o set o.executed =:executed where o.id=:oid")
    @Transactional
    Integer updateExecuted(Integer oid, Double executed);
}
