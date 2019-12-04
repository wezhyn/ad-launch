package com.ad.admain.to;

import com.ad.admain.common.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @date 2019/10/31
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity(name="ad_equipment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
public class Equipment implements IBaseTo<Integer> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private Integer uid;
    @ColumnDefault("'暂无介绍'")
    private String intro;

    private String img;
    @ColumnDefault("'暂无'")
    private String name;

    @ColumnDefault("0")
    private Double latitude;

    @ColumnDefault("0")
    private Double longitude;

    @Column(name="`key`")
    private String key;

    @Column(insertable=false, updatable=false)
    @ColumnDefault("current_timestamp")
    private LocalDateTime createTime;

    @ColumnDefault("current_timestamp")
    private LocalDateTime startTime;

    private LocalDateTime endTime;


    @ColumnDefault("1")
    private Boolean status;
    @ColumnDefault("0")
    private Boolean verify;


    public static Equipment createFromUid(Integer uid) {
        return Equipment.builder().uid(uid)
                .build();
    }


}
