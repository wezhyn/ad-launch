package com.ad.admain.security.filter;

import com.ad.admain.security.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezhyn.project.controller.ResponseResult;
import com.wezhyn.project.utils.HttpServletRequests;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * 用户认证失败，返回信息处理
 *
 * @author wezhyn
 * @since 12.26.2019
 */
public class JwtAuthenticationFailFilter extends GenericFilterBean {

    public static final int DEFAULT_CODE=50008;
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) res;
        try {
            Charset requestCharset=HttpServletRequests.getRequestCharset(request);
            response.setCharacterEncoding(requestCharset.name());
            response.setContentType("application/json;charset=" + requestCharset.name());
            chain.doFilter(request, response);
        } catch (JwtAuthenticationException authentication) {
            ResponseResult responseResult=ResponseResult.forFailureBuilder(DEFAULT_CODE)
                    .withMessage(authentication.getMessage())
                    .build();
            response.setStatus(200);
            PrintWriter writer=response.getWriter();
            writer.print(OBJECT_MAPPER.writeValueAsString(responseResult));
            writer.flush();
        }
    }
}
