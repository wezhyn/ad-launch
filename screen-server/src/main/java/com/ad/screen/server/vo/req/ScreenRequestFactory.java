package com.ad.screen.server.vo.req;

import com.ad.screen.server.vo.FrameType;

/**
 * @author wezhyn
 * @since 03.30.2020
 */
public class ScreenRequestFactory {


    public static BaseScreenRequest<?> createRequest(String equipName, FrameType type, Object data) {
        switch (type) {
            case GPS: {
                return new GpsMsg(equipName, (Point2D) data);
            }
            case CONFIRM: {
                return new ConfirmMsg(equipName);
            }
            case HEART_BEAT: {
                return new HeartBeatMsg(equipName);
            }
            case COMPLETE_NOTIFICATION: {
                return new CompleteNotificationMsg(equipName, (Integer) data);
            }
            default: {
                throw new RuntimeException("不支持的请求类型");
            }
        }
    }
}
