package com.ad.screen.server.vo.resp;

import com.ad.screen.server.vo.FrameType;
import com.ad.screen.server.vo.IScreenFrame;
import com.ad.screen.server.vo.IScreenFrameServer;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public abstract class  AbstractScreenResponse implements IScreenFrameServer {

    private String imei;
    private FrameType responseType;
    private LocalDateTime createTime=LocalDateTime.now();

    public AbstractScreenResponse(String imei, FrameType responseType) {
        this.imei=imei;
        this.responseType=responseType;
    }


    @Override
    public LocalDateTime serverTime() {
        return createTime;
    }

    @Override
    public int type() {
        return responseType.getType();
    }

    @Override
    public String equipmentImei() {
        return imei;
    }

    @Override
    public abstract String netData();
}
