package com.ad.screen.client.vo.resp;


import com.ad.screen.client.vo.IScreenFrameServer;
import com.ad.screen.client.vo.req.BaseScreenRequest;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public abstract class AbstractScreenResponse<T> extends BaseScreenRequest<T> implements IScreenFrameServer {

    private LocalDateTime createTime=LocalDateTime.now();


    public AbstractScreenResponse() {
    }

    @Override
    public LocalDateTime serverTime() {
        return createTime;
    }


    @Override
    public abstract String netData();
}
