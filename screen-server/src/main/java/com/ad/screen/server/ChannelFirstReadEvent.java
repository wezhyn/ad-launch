package com.ad.screen.server;

/**
 * 见 {@link com.ad.screen.server.handler.CompensateHandler}
 *
 * @author wezhyn
 * @since 03.26.2020
 */
public final class ChannelFirstReadEvent {

    public static final ChannelFirstReadEvent INSTANCE = new ChannelFirstReadEvent();

    private ChannelFirstReadEvent() {
    }
}
