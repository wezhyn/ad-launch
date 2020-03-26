package com.ad.screen.server.mq;

/**
 * @author wezhyn
 * @since 03.26.2020
 */
public interface PrepareTaskMessage {

    Integer getDeliverNum();

    Integer getRate();

    Double getLongitude();

    Double getLatitude();

    Double getScope();

}
