package com.ad.admain.screen.vo;

import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public enum FrameType {

    /**
     * 帧类型
     */
    HEART_BEAT(2), CONFIRM(1), GPS(3), IP(2), AD(3), COMPLETE_NOTIFICATION(4);


    @Getter
    private int type;

    FrameType(int i) {
        type=i;
    }

    public static FrameType parse(char index) {
        return Stream.of(FrameType.values())
                .filter(f->Objects.equals(((char) f.getType() + 48), index))
                .findFirst()
                .orElseThrow(()->new RuntimeException("无法解析的数据类型"));
    }


}
