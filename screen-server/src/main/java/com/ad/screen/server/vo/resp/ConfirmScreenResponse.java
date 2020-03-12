package com.ad.screen.server.vo.resp;

import com.ad.screen.server.vo.FrameType;
import com.ad.screen.server.vo.IScreenFrame;

/**
 * @author wezhyn
 * @since 02.19.2020
 */


public class ConfirmScreenResponse extends AbstractScreenResponse {

    public ConfirmScreenResponse(IScreenFrame request) {
        super(request, FrameType.CONFIRM);
    }

    @Override
    public String netData() {
        return "";
    }
}
