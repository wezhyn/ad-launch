package com.ad.screen.server.vo;

import java.time.LocalDateTime;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public interface IScreenFrameServer extends IScreenFrame {

    /**
     * 服务器时间
     *
     * @return 需要格式化为 20190628123040
     */
    LocalDateTime serverTime();
}
