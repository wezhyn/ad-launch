package com.ad.admain.security.filter;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * 通过组合模式实现
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class AntPathRequestMatcherExtractor implements RequestMatcher {

    private List<RequestMatcher> requestMatchers;


    public AntPathRequestMatcherExtractor(List<RequestMatcher> requestMatchers) {
        this.requestMatchers=requestMatchers;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for(RequestMatcher requestMatcher : requestMatchers) {
            if (requestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}
