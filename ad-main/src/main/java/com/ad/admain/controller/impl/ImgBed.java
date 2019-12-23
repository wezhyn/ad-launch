package com.ad.admain.controller.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wezhyn.project.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author wezhyn
 * @date 2019/09/29
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Entity
@Table(name="ad_img_bed")
@Data
@Builder(builderClassName="Builder")
@AllArgsConstructor
@NoArgsConstructor
public class ImgBed implements IBaseTo<Integer> {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;


    /**
     * 图片类别
     */
    @Enumerated(value=EnumType.STRING)
    @JsonProperty(access=JsonProperty.Access.READ_WRITE)
    private ImgBedType type;

    /**
     * 该类型的第几张
     */
    @Column(name="t_index")
    private Integer index;

    /**
     * 上传的文件名
     */
    private String filename;

    /**
     * 存储地址 {@link IFileUpload#getRelativeName()}
     */
    private String address;


    private ImgBed(Builder builder) {
        setId(builder.id);
        setType(builder.type);
        setFilename(builder.filename);
        setAddress(builder.address);
        setIndex(builder.index);
    }


    public static ImgBed forGuide(int index, String fileName, String address) {
        return new Builder()
                .index(index)
                .address(address)
                .filename(fileName)
                .type(ImgBedType.GUIDE)
                .build();
    }

    public static ImgBed forShuffing(String fileName, String address) {
        return new Builder()
                .address(address)
                .filename(fileName)
                .type(ImgBedType.SHUFFING)
                .build();
    }

    public static ImgBed forAvatar(String fileName, String address) {
        return new Builder()
                .address(address)
                .filename(fileName)
                .type(ImgBedType.SHUFFING)
                .build();
    }


}
