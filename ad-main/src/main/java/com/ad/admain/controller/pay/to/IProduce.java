package com.ad.admain.controller.pay.to;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 适配之前的 AdOrder
 *
 * @author wezhyn
 * @since 02.25.2020
 */
public interface IProduce {


    List<String> getProduceContext();

    Integer getDeliverNum();

    Double getPrice();

    Integer getNum();

    Double getLatitude();

    Double getLongitude();

    Double getScope();

    Integer getRate();

    LocalDate getStartDate();

    LocalDate getEndDate();

    LocalTime getStartTime();

    LocalTime getEndTime();
}
