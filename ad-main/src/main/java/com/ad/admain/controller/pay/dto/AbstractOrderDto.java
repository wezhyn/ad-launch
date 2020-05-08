package com.ad.admain.controller.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wezhyn
 * @since 05.07.2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbstractOrderDto {

    private int id;


    private int uid;

    /**
     * 代客服审核
     */
    private String verify;

    private int produceId;
    private String tradeOut;

    /**
     * 广告内容, 前端返回 List<String>
     * 由 | 分割
     */
    @JsonProperty("valueList")
    private List<String> produceContext;

    /**
     * 要求广告投放到车上的数量
     */
    private Integer deliverNum;

    /**
     * 订单单价
     */
    @NotNull
    private Double totalAmount;

    /**
     * 订单数量
     */
    @NotNull
    private Integer num;

    private Double latitude;

    private Double longitude;

    private boolean vertical;

    private String orderStatus;

    /**
     * 广告投放范围
     */

    private Double scope;

    /**
     * 广告投放频率
     */
    private Integer rate;

    private Double price;


    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;

    private String createTime;
}
