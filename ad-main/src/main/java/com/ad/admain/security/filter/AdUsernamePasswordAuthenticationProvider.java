package com.ad.admain.security.filter;

import com.ad.admain.exception.AdUsernamePasswordException;
import com.ad.admain.security.AdAuthentication;
import com.ad.admain.security.AdUserDetails;
import com.ad.admain.security.AdUserDetailsService;
import com.ad.admain.security.MarkAntPathRequestMatcherExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @author wezhyn
 * 登录时获取的userDetails 类型为{@link AdUserDetails}
 * 验证条件：验证id，验证密码
 */
public class AdUsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private List<AdUserDetailsService> userDetailsServices;
    private PasswordEncoder passwordEncoder;
    private UserDetailsChecker preAuthenticationChecks=new DefaultPreAuthenticationChecks();
    private final Logger logger=LoggerFactory.getLogger(this.getClass());
    private GrantedAuthoritiesMapper authoritiesMapper=new NullAuthoritiesMapper();

    public AdUsernamePasswordAuthenticationProvider(PasswordEncoder passwordEncoder, List<AdUserDetailsService> userDetailsServices) {
        this.userDetailsServices=userDetailsServices;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final AdUserDetails adUserDetails=(AdUserDetails) userDetails;
//        check id
        if (adUserDetails.getId()==null || Integer.valueOf(1).equals(adUserDetails.getId())) {
            throw new AdUsernamePasswordException("用户信息异常");
        }
//        check password
        if (authentication.getCredentials()==null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new AdUsernamePasswordException("密码错误");
        } else {
            String presentedPassword=authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, adUserDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new AdUsernamePasswordException("密码错误");
            }
        }
    }

    /**
     * 返回的认证认证信息
     *
     * @param principal
     * @param authentication
     * @param user
     * @return
     */
    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        AdAuthentication result=new AdAuthentication(
                principal, authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    /**
     * 禁止将认证信息强制转化为 getName();
     *
     * @param forcePrincipalAsString force
     */
    @Override
    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        super.setForcePrincipalAsString(false);
    }

    @Override
    protected AdUserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AdAuthentication auth=(AdAuthentication) authentication;

        AdUserDetails user=null;
        String mark=MarkAntPathRequestMatcherExtractor.getMarkCache().get();
        for (AdUserDetailsService userDetailsService : userDetailsServices) {
            if (userDetailsService.support(mark==null ? "" : mark)) {
                user=userDetailsService.loadUserByUsername(username);
                this.preAuthenticationChecks.check(user);
            }
        }
        MarkAntPathRequestMatcherExtractor.getMarkCache().remove();
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AdAuthentication.class.isAssignableFrom(authentication);
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        private DefaultPreAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");
                throw new LockedException("User account is locked");
            } else if (!user.isEnabled()) {
                logger.debug("User account is disabled");
                throw new DisabledException("User is disabled");
            } else if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");
                throw new AccountExpiredException("User account has expired");
            }
        }

    }
}
