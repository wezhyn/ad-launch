package com.ad.admain.security.filter;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class HttpStatusFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp=(HttpServletResponse) servletResponse;
        try {
            filterChain.doFilter(servletRequest, servletResponse);
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(200);
        }
    }
}
