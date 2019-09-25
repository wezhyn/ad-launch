package com.ad.admain.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wezhyn
 * @date : 2019/09/21
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Data
@ConfigurationProperties(prefix="custom.qn")
public class QiNiuProperties {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String hostName;


}
