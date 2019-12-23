package com.ad.admain.controller.pay.dto;

import com.ad.admain.controller.pay.to.Value;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Accessors(chain=true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private int id;

    private List<Value> valueList;

    private Date startTime;
    private Date endTime;
    private Date startDate;
    private Date endDate;

    /**
     * 订单单价
     */
    private Double price;

    private Integer num;

    private double latitude;

    private double longitude;

    /**
     * 广告投放范围
     */
    private int scope;

    /**
     * 广告投放频率
     */
    private int rate;

    private int uid;

    /**
     * 代客服审核
     */
    private Boolean verify;


}
