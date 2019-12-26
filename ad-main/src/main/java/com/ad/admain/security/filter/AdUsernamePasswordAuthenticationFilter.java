package com.ad.admain.security.filter;

import com.ad.admain.security.AdAuthentication;
import com.ad.admain.security.IUsernamePasswordConvert;
import com.ad.admain.security.MarkAntPathRequestMatcherExtractor;
import com.ad.admain.security.exception.AdUsernamePasswordException;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * 通过 手动创建
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class AdUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private final List<IUsernamePasswordConvert> usernamePasswordConverts;
    private final AuthenticationManager authenticationManager;


    public AdUsernamePasswordAuthenticationFilter(Map<String, String> matchs, List<IUsernamePasswordConvert> usernamePasswordConverts, AuthenticationManager authenticationManager) {
        super("/login");
        this.usernamePasswordConverts=usernamePasswordConverts;
        this.authenticationManager=authenticationManager;
        setRequiresAuthenticationRequestMatcher(new MarkAntPathRequestMatcherExtractor(matchs));
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
            throw new AdUsernamePasswordException("从请求中读取账户信息出错");
        }
        AdAuthentication authRequest=new AdAuthentication(definition.getUsername(), definition.getPassword());
        try {
            return this.authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            throw new AdUsernamePasswordException(e.getMessage());
        }
    }


    @Data
    public static class UsernamePasswordDefinition {
        private String username;
        private String password;
    }

}
