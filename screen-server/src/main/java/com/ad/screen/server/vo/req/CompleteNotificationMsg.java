package com.ad.screen.server.vo.req;

import com.ad.launch.order.AdStringUtils;
import com.ad.screen.server.vo.FrameType;

/**
 * @author wezhyn
 * @since 03.08.2020
 */
public class CompleteNotificationMsg extends BaseScreenRequest<Integer> {
    public CompleteNotificationMsg(String equipmentName, Integer netData) {
        super(equipmentName, FrameType.COMPLETE_NOTIFICATION, netData);
    }

    @Override
    public String netData() {
        return AdStringUtils.getNum(getNetData(), 4);
    }
}
