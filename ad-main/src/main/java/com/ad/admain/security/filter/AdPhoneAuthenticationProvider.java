package com.ad.admain.security.filter;

import com.ad.admain.controller.ISmsService;
import com.ad.admain.security.AdPhoneAuthentication;
import com.ad.admain.security.AdUserDetails;
import com.ad.admain.security.AdUserDetailsService;
import com.ad.admain.security.exception.AdUsernamePasswordException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
public class AdPhoneAuthenticationProvider extends AdUsernamePasswordAuthenticationProvider {


    private ISmsService smsService;

    public AdPhoneAuthenticationProvider(ISmsService service, List<AdUserDetailsService> userDetailsServices) {
//        用于不需要判断用户密码是否正确
        super(null, userDetailsServices);
        this.smsService=service;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final AdUserDetails adUserDetails=(AdUserDetails) userDetails;
        final AdPhoneAuthentication phoneAuthentication=(AdPhoneAuthentication) authentication;
        Assert.notNull(phoneAuthentication, "系统异常");
        try {
            if (phoneAuthentication.getCredentials()==null) {
//            未有验证码，发送短信
                smsService.send((String) phoneAuthentication.getPrincipal(), ISmsService.SmsType.LOGIN);
            } else {
                smsService.verifyCode((String) phoneAuthentication.getPrincipal(), (String) phoneAuthentication.getCredentials(), ISmsService.SmsType.LOGIN);
            }
        } catch (Exception e) {
            throw new AdUsernamePasswordException(e.getMessage());
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        final AdPhoneAuthentication phoneAuthentication=(AdPhoneAuthentication) authentication;
        if (phoneAuthentication.getCredentials()!=null) {
            return super.createSuccessAuthentication(principal, authentication, user);
        }
        return phoneAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AdPhoneAuthentication.class==authentication;
    }
}
