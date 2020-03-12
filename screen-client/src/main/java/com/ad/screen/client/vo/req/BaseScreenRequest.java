package com.ad.screen.client.vo.req;

import com.ad.screen.client.vo.FrameType;
import com.ad.screen.client.vo.IScreenFrame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@AllArgsConstructor
@Builder
@Data
public class BaseScreenRequest<T> implements IScreenFrame {


    /**
     * IMEI
     */
    private String equipmentName;

    /**
     * 帧类型
     */
    private FrameType frameType;

    /**
     * 前一个数表示经度
     * 后一个数表示维度：+
     */
    private T netData;

    public BaseScreenRequest() {
    }

    public BaseScreenRequest(T netData) {
        this.netData=netData;
    }

    @Override
    public int type() {
        return frameType.getType();
    }

    @Override
    public String equipmentImei() {
        return equipmentName;
    }

    @Override
    public String netData() {
        return "";
    }
}
