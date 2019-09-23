package com.ad.adlaunch.security.filter;

import com.ad.adlaunch.security.AdUserNamePasswordAuthenticationToken;
import com.ad.adlaunch.security.IUsernamePasswordConvert;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 *     通过 手动创建
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class AdUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private final List<IUsernamePasswordConvert> usernamePasswordConverts;
    private final AuthenticationManager authenticationManager;


    public AdUsernamePasswordAuthenticationFilter(String match,List<IUsernamePasswordConvert> usernamePasswordConverts,AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(match,null,false));
        this.usernamePasswordConverts=usernamePasswordConverts;
        this.authenticationManager=authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        MediaType mediaType=MediaType.parseMediaType(request.getContentType());
        UsernamePasswordDefinition definition=null;
        for (IUsernamePasswordConvert usernamePasswordConvert : usernamePasswordConverts) {
            if (usernamePasswordConvert.support(mediaType)) {
                 definition=usernamePasswordConvert.convert(request);
                 break;
            }
        }
        if (definition==null) {
            throw new IOException("从请求中读取账户信息出错");
        }
        AdUserNamePasswordAuthenticationToken authRequest=new AdUserNamePasswordAuthenticationToken(definition.getUsername(), definition.getPassword());
        return this.authenticationManager.authenticate(authRequest);
    }


    @Data
    public static class UsernamePasswordDefinition{
        private String username;
        private String password;
    }

}
