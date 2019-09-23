package com.ad.adlaunch.constants;

import lombok.Data;
import org.hibernate.validator.constraints.EAN;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@ConfigurationProperties(prefix="custom.jwt")
@Data
public class JwtProperties {

    private int defaultHours=1;
    private int rememberMeDay=1;

    private String checkHeader="X-Token";

    private List<String> checkListExclusion;

    private List<String > loginInterceptionInclude;

}
