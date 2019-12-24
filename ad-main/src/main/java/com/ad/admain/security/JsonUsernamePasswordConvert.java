package com.ad.admain.security;

import com.ad.admain.security.filter.AdUsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 支持MediaType 为 json 类型的Request
 * </p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Component
public class JsonUsernamePasswordConvert implements IUsernamePasswordConvert {

    private final static List<MediaType> SUPPORT_MEDIA_TYPES;

    static {
        SUPPORT_MEDIA_TYPES=Collections.unmodifiableList(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
    }

    private final ObjectMapper objectMapper;


    public JsonUsernamePasswordConvert(ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
    }

    @Override
    public List<MediaType> getMediaTypes() {
        return SUPPORT_MEDIA_TYPES;
    }

    @Override
    public AdUsernamePasswordAuthenticationFilter.UsernamePasswordDefinition convert(HttpServletRequest request) throws IOException {
        return this.objectMapper.readValue(request.getReader(), AdUsernamePasswordAuthenticationFilter.UsernamePasswordDefinition.class);
    }
}
