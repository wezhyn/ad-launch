package com.ad.admain.security;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wezhyn
 * @date 2019/10/31
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class AntPathRequestMatcherExtractor implements RequestMatcher {

    private List<RequestMatcher> antPathMatchers;

    public AntPathRequestMatcherExtractor(List<RequestMatcher> antPathMatchers) {
        Assert.notNull(antPathMatchers, "拦截列表为空");
        this.antPathMatchers=antPathMatchers;
    }


    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher matcher : antPathMatchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}
