package com.ad.admain.controller.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Accessors(chain=true)
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderDto {

    private int id;


    private int uid;

    /**
     * 代客服审核
     */
    private String verify;

    private int produceId;
    private long tradeOut;

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


    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;

    private String createTime;


}
