package com.ad.adlaunch.security.filter;

import com.ad.adlaunch.security.AdNamePasswordAuthenticationToken;
import com.ad.adlaunch.security.IUsernamePasswordConvert;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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


    public AdUsernamePasswordAuthenticationFilter(List<String > matchs,List<IUsernamePasswordConvert> usernamePasswordConverts,AuthenticationManager authenticationManager) {
        super("/api/user/login");
        List<RequestMatcher> requestMatchers=new ArrayList<>(3);
        for (String match : matchs) {
            requestMatchers.add(new AntPathRequestMatcher(match,null,false));
        }
        this.usernamePasswordConverts=usernamePasswordConverts;
        this.authenticationManager=authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcherExtractor(requestMatchers));
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
        AdNamePasswordAuthenticationToken authRequest=new AdNamePasswordAuthenticationToken(definition.getUsername(), definition.getPassword(),request.getRequestURI());
        return this.authenticationManager.authenticate(authRequest);
    }


    @Data
    public static class UsernamePasswordDefinition{
        private String username;
        private String password;
    }

}
