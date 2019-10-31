package com.ad.admain.to;

import com.ad.admain.common.IBaseTo;
import com.ad.admain.dto.IFileUpload;
import com.ad.admain.enumate.ImgBedType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    private int index;

    /**
     * 上传的文件名
     */
    @JsonProperty(value="key")
    private String filename;

    /**
     * 存储地址 {@link IFileUpload#getRelativeName()}
     */
    @JsonProperty("value")
    private String address;

    @Override
    public String getUsername() {
        return getId().toString();
    }

    private ImgBed(Builder builder) {
        setId(builder.id);
        setType(builder.type);
        setFilename(builder.key);
        setAddress(builder.address);
        setIndex(builder.index);
    }

    private ImgBed() {
    }

    public static ImgBed forGuide(int index, String fileName, String address) {
        return new Builder()
                .index(index)
                .address(address)
                .key(fileName)
                .type(ImgBedType.GUIDE)
                .build();
    }

    public static ImgBed forShuffing(String fileName, String address) {
        return new Builder()
                .address(address)
                .key(fileName)
                .type(ImgBedType.SHUFFING)
                .build();
    }


    private static final class Builder {
        private Integer id;
        private ImgBedType type;
        private String key;
        private String address;
        private int index;

        public Builder() {
        }


        public Builder index(int index) {
            this.index=index;
            return this;
        }

        public Builder id(Integer val) {
            id=val;
            return this;
        }

        public Builder type(ImgBedType val) {
            type=val;
            return this;
        }

        public Builder key(String val) {
            key=val;
            return this;
        }

        public Builder address(String val) {
            address=val;
            return this;
        }

        public ImgBed build() {
            return new ImgBed(this);
        }
    }
}
