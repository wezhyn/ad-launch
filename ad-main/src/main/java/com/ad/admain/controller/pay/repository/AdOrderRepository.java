package com.ad.admain.controller.pay.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.ad.admain.controller.pay.dto.ITopUserNum;
import com.ad.admain.controller.pay.to.AdOrder;
import com.ad.admain.controller.pay.to.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Modifying
    @Query(nativeQuery = true, value = "update ad_order o set o.verify= :verify , o.feedback=:feedback where id=:oId")
    Integer verifyOrder(Integer oId, String feedback, Integer verify);

    @Query(nativeQuery = true, value = "select uid,count(*) as 'num' from ad_order  group by uid")
    List<ITopUserNum> topAd();

    /**
     * 更新订单属性
     *
     * @param orderId      订单id
     * @param originStatus 原状态
     * @param nextStatus   下一个状态
     * @return 1
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
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
    @Modifying(clearAutomatically = true, flushAutomatically = true)
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

    @Query(nativeQuery = true, value = "select sum(total_amount) from ad_order where uid=:userId ")
    double getUserAmount(Integer userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ad_order o set o.executed =:executed where o.id=:oid")
    @Transactional
    Integer updateExecuted(Integer oid, Integer executed);

    List<AdOrder> findAdOrdersByOrderStatusEquals(OrderStatus orderStatus);

    Page<AdOrder> findByOrderStatusGreaterThanAndIsDeleteIsFalse(OrderStatus orderStatus, Pageable pageable);
}
