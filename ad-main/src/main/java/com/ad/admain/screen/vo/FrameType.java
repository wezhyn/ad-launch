package com.ad.admain.screen.vo;

import lombok.Getter;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public enum FrameType {

    /**
     * 帧类型
     */
    HEART_BEAT(2), CONFIRM(1), GPS(3), IP(2), AD(3);


    @Getter
    private int type;

    FrameType(int i) {
        type=i;
    }


}
