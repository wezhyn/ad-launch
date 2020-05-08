package com.ad.admain.controller.pay.to;


import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 商品内容
 *
 * @author : zlb
 * @date : 2019/12/31
 */
@Data
@Entity(name = "ad_produce")
@Builder
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class AdProduce implements IBaseTo<Integer>, IProduce {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    /**
     * 广告内容, 前端返回 List<String>
     * 由 | 分割
     */
    @Column(name = "context")
    @Convert(converter = ProduceContextConvert.class)
    private List<String> produceContext;
    /**
     * 要求广告投放到车上的数量
     */
    @ColumnDefault(value = "1")
    private Integer deliverNum;
    /**
     * 订单单价
     */
    @Column(nullable = false)
    private Double price;
    /**
     * 订单数量
     */
    @Column(nullable = false)
    private Integer num;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    @Column(nullable = false)
    private Boolean vertical;
    /**
     * 广告投放范围
     */
    @Column(nullable = false)
    private Double scope;
    /**
     * 广告投放频率
     */
    @Column(nullable = false)
    private Integer rate;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;

    public AdProduce(Integer id) {
        this.id = id;
    }

    public AdProduce() {
    }


    @Override
    public List<String> getProduceContext() {
        return produceContext;
    }

    @Override
    public Integer getDeliverNum() {
        return deliverNum;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Integer getNum() {
        return num;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public Double getScope() {
        return scope;
    }

    @Override
    public Integer getRate() {
        return rate;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

}
