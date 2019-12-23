package com.ad.admain.security;

import com.ad.admain.controller.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当登录失败后的处理器
 *
 * @author : wezhyn
 * @date : 2019/09/19
 */
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler, LoginAuthenticationCleanHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        clean();
        String errMsg=exception.getMessage();
        exception.printStackTrace();
        ResponseResult responseResult=ResponseResult
                .forFailureBuilder()
                .withMessage(errMsg).build();
        response.getWriter().write(this.objectMapper.writeValueAsString(responseResult));
    }

    public LoginAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
    }

    @Override
    public void clean() {
        MarkAntPathRequestMatcherExtractor.removeThreadLocal();
    }
}
