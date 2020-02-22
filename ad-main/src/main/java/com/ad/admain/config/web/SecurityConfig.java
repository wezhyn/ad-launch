package com.ad.admain.config.web;

import com.ad.admain.config.JwtConfig;
import com.ad.admain.controller.ISmsService;
import com.ad.admain.security.*;
import com.ad.admain.security.filter.*;
import com.ad.admain.security.jwt.JwtDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdJwtCheckAuthenticationFilter jwtCheckAuthenticationFilter;
    @Autowired
    private List<IUsernamePasswordConvert> usernamePasswordConverts;
    @Autowired
    private LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;
    @Autowired
    private LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;
    @Autowired
    private JwtDetailService jwtDetailService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private List<AdUserDetailsService> adUserDetailsServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ISmsService smsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        AdUsernamePasswordAuthenticationProvider adUsernamePasswordAuthenticationProvider=new AdUsernamePasswordAuthenticationProvider(passwordEncoder, adUserDetailsServices);
        AdPhoneAuthenticationProvider adPhoneAuthenticationProvider=new AdPhoneAuthenticationProvider(smsService, adUserDetailsServices);
        auth
                .authenticationProvider(adPhoneAuthenticationProvider)
                .authenticationProvider(adUsernamePasswordAuthenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/" + ResourceConstant.RESOURCE + "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Map<String, String> matchs=jwtProperties.getLoginInterceptionInclude();
        Filter authenticationFilter=new JwtAuthenticationFailFilter();
        AdUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter=createUserNamePasswordAuthenticationFilter(
                matchs, authenticationManager(), usernamePasswordConverts,
                loginAuthenticationFailureHandler,
                loginAuthenticationSuccessHandler);
        http.cors().and()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(usernamePasswordAuthenticationFilter, LogoutFilter.class)
                .addFilterBefore(jwtCheckAuthenticationFilter, usernamePasswordAuthenticationFilter.getClass())
                .addFilterBefore(new HttpStatusFilter(), HeaderWriterFilter.class)
                .addFilterAt(adJwtLogoutAuthenticationFilter(jwtProperties.getLogoutInterception(), jwtDetailService), LogoutFilter.class)
                .addFilterBefore(authenticationFilter, AdJwtCheckAuthenticationFilter.class)
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/").permitAll();

    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("PUT", "POST", "GET", "OPTIONS", "DELETE"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /**
     * 创建 登录拦截器 拦截请求：/api/user/login
     *
     * @param authenticationManager    authenticationManager
     * @param usernamePasswordConverts converts
     * @param successHandler           登录成功后的处理 {@link JwtConfig#loginAuthenticationSuccessHandler}
     * @param failureHandler           登录失败后的处理 {@link JwtConfig#loginAuthenticationFailureHandler}
     * @return filter
     */
    public AdUsernamePasswordAuthenticationFilter createUserNamePasswordAuthenticationFilter(
            Map<String, String> matchs,
            AuthenticationManager authenticationManager,
            List<IUsernamePasswordConvert> usernamePasswordConverts,
            LoginAuthenticationFailureHandler failureHandler,
            LoginAuthenticationSuccessHandler successHandler) {
        AdUsernamePasswordAuthenticationFilter filter=new AdUsernamePasswordAuthenticationFilter(matchs, usernamePasswordConverts, authenticationManager);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationSuccessHandler(successHandler);
        return filter;

    }

    private AdJwtLogoutAuthenticationFilter adJwtLogoutAuthenticationFilter(List<String> matchs, JwtDetailService jwtDetailService) {
        LogoutAuthenticationSuccessHandler successHandler=new LogoutAuthenticationSuccessHandler(jwtDetailService);
        return new AdJwtLogoutAuthenticationFilter(matchs, successHandler);
    }
}
