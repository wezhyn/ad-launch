package com.ad.adlaunch.security;

import com.ad.adlaunch.dto.ResponseResult;
import com.ad.adlaunch.security.jwt.SecurityJwtProvider;
import com.ad.adlaunch.service.GenericUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.util.RequestUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 */
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private SecurityJwtProvider securityJwtProvider;

    private ObjectMapper objectMapper;
    public LoginAuthenticationSuccessHandler(SecurityJwtProvider securityJwtProvider,ObjectMapper objectMapper) {
        this.securityJwtProvider=securityJwtProvider;
        this.objectMapper=objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter=response.getWriter();
        String token=securityJwtProvider.createToken(authentication, false, authentication.getName());
        ResponseResult result=ResponseResult.forSuccessBuilder()
                .withData("token", token).build();
        write(result,request,response);
    }

    private  void write(ResponseResult result,HttpServletRequest request,HttpServletResponse response) throws IOException {
        String r=objectMapper.writeValueAsString(result);
        try(PrintWriter printWriter=response.getWriter()) {
            printWriter.write(r);
        }
    }
}
