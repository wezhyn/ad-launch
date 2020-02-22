package com.ad.admain.screen.vo.resp;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.IScreenFrame;
import com.ad.admain.screen.vo.IScreenFrameServer;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public abstract class AbstractScreenResponse implements IScreenFrameServer {

    private IScreenFrame request;
    private FrameType responseType;
    private LocalDateTime createTime=LocalDateTime.now();

    public AbstractScreenResponse(IScreenFrame request, FrameType responseType) {
        this.request=request;
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
        return request.equipmentImei();
    }

    @Override
    public abstract String netData();
}
