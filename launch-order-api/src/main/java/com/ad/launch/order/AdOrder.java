package com.ad.launch.order;

import com.ad.launch.user.AdUser;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Getter
public class AdOrder {

    private Integer id;


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

}
