package com.ad.screen.server.cache;

/**
 * 通道关闭
 * 用于使用事件监听模式，无法使用受检异常
 *
 * @author wezhyn
 * @since 03.28.2020
 */
public class ChannelCloseException extends RuntimeException {

    public ChannelCloseException() {
        super("当前通道关闭");
    }
}
