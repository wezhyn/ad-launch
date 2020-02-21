package com.ad.admain.screen.vo.resp;

import com.ad.admain.screen.vo.FrameType;
import com.ad.admain.screen.vo.IScreenFrame;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
@Component
public class ConfirmScreenResponse extends AbstractScreenResponse {

    public ConfirmScreenResponse(IScreenFrame request) {
        super(request, FrameType.CONFIRM);
    }
    @Override
    public String netData() {
        return "";
    }
}
