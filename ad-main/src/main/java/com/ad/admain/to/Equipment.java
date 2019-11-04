package com.ad.admain.to;

import com.ad.admain.common.IBaseTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

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
public class Equipment implements IBaseTo<Integer> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private Integer uid;
    private String intro;
    private String img;

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

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    @ColumnDefault("1")
    private boolean status;
    @ColumnDefault("0")
    private boolean verify;

    private Equipment(Builder builder) {
        setId(builder.id);
        setUid(builder.uid);
        setIntro(builder.intro);
        setImg(builder.img);
        setName(builder.name);
        setLatitude(builder.latitude);
        setLongitude(builder.longitude);
        setKey(builder.key);
        setCreateTime(builder.createTime);
        setStartTime(builder.startTime);
        setEndTime(builder.endTime);
        setStatus(builder.status);
        setVerify(builder.verify);
    }

    @Override
    public String getUsername() {
        return name;
    }

    public static Equipment createFromUid(Integer uid) {
        return Equipment.builder().uid(uid)
                .status(true)
                .verify(false)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private Integer id;
        private Integer uid;
        private String intro;
        private String img;
        private String name;
        private String position;
        private Double latitude;
        private Double longitude;
        private String key;
        private LocalDateTime createTime;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean status;
        private boolean verify;

        public Builder() {
        }

        public Builder id(Integer val) {
            id=val;
            return this;
        }

        public Builder uid(Integer val) {
            uid=val;
            return this;
        }

        public Builder intro(String val) {
            intro=val;
            return this;
        }

        public Builder img(String val) {
            img=val;
            return this;
        }

        public Builder name(String val) {
            name=val;
            return this;
        }


        public Builder latitude(Double val) {
            if (val==null) {
                val=(double) 0;
            }
            latitude=val;
            return this;
        }

        public Builder longitude(Double val) {
            if (val==null) {
                val=(double) 0;
            }
            longitude=val;
            return this;
        }

        public Builder key(String val) {
            key=val;
            return this;
        }

        public Builder createTime(LocalDateTime val) {
            createTime=val;
            return this;
        }

        public Builder startTime(LocalDateTime val) {
            startTime=val;
            return this;
        }

        public Builder endTime(LocalDateTime val) {
            endTime=val;
            return this;
        }

        public Builder status(boolean val) {
            status=val;
            return this;
        }

        public Builder verify(boolean val) {
            verify=val;
            return this;
        }

        public Equipment build() {
            return new Equipment(this);
        }
    }
}
