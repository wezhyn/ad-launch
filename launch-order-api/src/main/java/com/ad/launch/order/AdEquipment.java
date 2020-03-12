package com.ad.launch.order;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Setter
@Getter
public class AdEquipment implements Serializable {


    private static final long serialVersionUID=7186787979907565438L;
    private Integer id;

    private Integer uid;

    private String intro;

    private String img;
    private String name;

    private Double latitude;

    private Double longitude;

    private String key;

    private LocalDateTime createTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean status;

    private EquipmentVerify verify;

    private String feedback;


    private Integer remain;


}
