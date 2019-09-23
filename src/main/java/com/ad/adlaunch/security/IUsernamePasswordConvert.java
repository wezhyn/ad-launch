package com.ad.adlaunch.security;

import com.ad.adlaunch.security.filter.AdUsernamePasswordAuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 从Request中提取信息
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public interface IUsernamePasswordConvert {

    /**
     * 是否支持当前 mediaType的解析
     *
     * @param mediaType mediaType
     * @return true
     */
    default boolean support(MediaType mediaType) {
        for (MediaType supportedMediaType : getMediaTypes()) {
            if (supportedMediaType.includes(mediaType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取支持的MediaType类型
     *
     * @return mediaType
     */
    List<MediaType> getMediaTypes();

    /**
     * 从 request中提取 userName password
     *
     * @param request request
     * @return usernamePasswordDefinition
     */
    AdUsernamePasswordAuthenticationFilter.UsernamePasswordDefinition convert(HttpServletRequest request) throws IOException;
}
