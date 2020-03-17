package com.ad.launch.order;


import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdProduce implements IBaseTo<Integer>, Serializable {

    Integer id;

    /**
     * 广告内容, 前端返回 List<String>
     * 由 | 分割
     */
    private List<String> produceContext;

    /**
     * 要求广告投放到车上的数量
     */
    private Integer deliverNum;

    /**
     * 订单单价
     */
    private Double price;

    /**
     * 订单数量
     */
    private Integer num;

    private Double latitude;

    private Double longitude;

    private Boolean vertical;

    /**
     * 广告投放范围
     */

    private Double scope;

    /**
     * 广告投放频率
     */
    private Integer rate;


    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;


}
