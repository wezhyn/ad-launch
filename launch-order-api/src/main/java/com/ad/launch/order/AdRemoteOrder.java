package com.ad.launch.order;

import com.ad.launch.user.AdUser;
import com.wezhyn.project.annotation.UpdateIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdRemoteOrder implements Serializable {

    private Integer id;

    @UpdateIgnore
    private AdUser orderUser;

    private Integer uid;


    private Double totalAmount;

    private Integer numPerEquip;

    private Long tradeOut;

    private LocalDateTime createTime;


    private LocalDateTime modifyTime;

    private Boolean isDelete;


    private AdProduce produce;

    private OrderStatus orderStatus;

    private Integer executed;


    public List<String> getProduceContext() {
        return produce.getProduceContext();
    }

    public Integer getDeliverNum() {
        return produce.getDeliverNum();
    }

    public Double getPrice() {
        return this.produce.getPrice();
    }

    public Integer getNum() {
        return produce.getNum();
    }

    public Double getLatitude() {
        return produce.getLatitude();
    }

    public Double getLongitude() {
        return produce.getLongitude();
    }

    public Double getScope() {
        return produce.getScope();
    }

    public Integer getRate() {
        return produce.getRate();
    }

    public LocalDate getStartDate() {
        return produce.getStartDate();
    }

    public LocalDate getEndDate() {
        return produce.getEndDate();
    }

    public LocalTime getStartTime() {
        return produce.getStartTime();
    }

    public LocalTime getEndTime() {
        return produce.getEndTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AdUser getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(AdUser orderUser) {
        this.orderUser = orderUser;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getNumPerEquip() {
        return numPerEquip;
    }

    public void setNumPerEquip(Integer numPerEquip) {
        this.numPerEquip = numPerEquip;
    }

    public Long getTradeOut() {
        return tradeOut;
    }

    public void setTradeOut(Long tradeOut) {
        this.tradeOut = tradeOut;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public AdProduce getProduce() {
        return produce;
    }

    public void setProduce(AdProduce produce) {
        this.produce = produce;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getExecuted() {
        return executed;
    }

    public void setExecuted(Integer executed) {
        this.executed = executed;
    }


}
