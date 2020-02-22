package com.ad.admain.security;

import com.ad.admain.config.web.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过组合模式实现
 *
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class MarkAntPathRequestMatcherExtractor implements RequestMatcher {


    private Map<String, RequestMatcher> requestMarkMatchers;

    /**
     * {@link JwtProperties#getLoginInterceptionInclude()}
     *
     * @param interceptMap map
     */
    public MarkAntPathRequestMatcherExtractor(Map<String, String> interceptMap) {
        if (interceptMap==null || interceptMap.size()==0) {
            throw new NullPointerException("无有效拦截列表");
        }
        requestMarkMatchers=new HashMap<>(7);
        for (String mark : interceptMap.keySet()) {
            log.info("mark: {} intercept : {}", mark, interceptMap.get(mark));
            requestMarkMatchers.put(mark,
                    new AntPathRequestMatcher(interceptMap.get(mark), null, false));
        }
    }


    @Override
    public boolean matches(HttpServletRequest request) {
        for (String mark : requestMarkMatchers.keySet()) {
            RequestMatcher matcher=requestMarkMatchers.get(mark);
            if (matcher.matches(request)) {
                request.setAttribute("mark", mark);
                return true;
            }
        }
        return false;
    }
}
