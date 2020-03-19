package com.ad.admain.controller.pay.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品内容
 *
 * @author : zlb
 * @date : 2019/12/31
 */
@Data
@Builder
public class AdProduceDto {

    Integer id;

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
    private Double price;

    /**
     * 订单数量
     */
    @NotNull
    private Integer num;

    private Double latitude;

    private Double longitude;

    private boolean vertical;


    /**
     * 广告投放范围
     */

    private Double scope;

    /**
     * 广告投放频率
     */
    private Integer rate;


    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private Double executed;

}
