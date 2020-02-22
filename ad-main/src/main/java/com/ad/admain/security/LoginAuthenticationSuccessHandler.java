package com.ad.admain.security;

import com.ad.admain.security.jwt.SecurityJwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wezhyn.project.controller.ResponseResult;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 */
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler, LoginAuthenticationCleanHandler {
    private SecurityJwtProvider securityJwtProvider;

    private ObjectMapper objectMapper;

    public LoginAuthenticationSuccessHandler(SecurityJwtProvider securityJwtProvider, ObjectMapper objectMapper) {
        this.securityJwtProvider=securityJwtProvider;
        this.objectMapper=objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        clean();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (AdPhoneAuthentication.class==authentication.getClass() && !authentication.isAuthenticated()) {
//            表示为发送手机验证码
            ResponseResult result=ResponseResult.forSuccessBuilder()
                    .withMessage("发送登录短信成功").build();
            write(result, request, response);
            return;
        }
        String token=securityJwtProvider.createToken(authentication, false, authentication.getName());
        ResponseResult result=ResponseResult.forSuccessBuilder()
                .withMessage("登录成功")
                .withData("token", token).build();
        write(result, request, response);
    }

    private void write(ResponseResult result, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String r=objectMapper.writeValueAsString(result);
        try (PrintWriter printWriter=response.getWriter()) {
            printWriter.write(r);
        }
    }


}
