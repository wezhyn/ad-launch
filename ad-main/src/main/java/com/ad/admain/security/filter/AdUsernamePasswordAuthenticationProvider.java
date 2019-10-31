package com.ad.admain.security.filter;

import com.ad.admain.exception.AdUsernamePasswordException;
import com.ad.admain.security.AdNamePasswordAuthenticationToken;
import com.ad.admain.service.AdUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @author wezhyn
 */
public class AdUsernamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private List<AdUserDetailsService> userDetailsServices;
    private PasswordEncoder passwordEncoder;
    private UserDetailsChecker preAuthenticationChecks=new DefaultPreAuthenticationChecks();
    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    public AdUsernamePasswordAuthenticationProvider(PasswordEncoder passwordEncoder, List<AdUserDetailsService> userDetailsServices) {
        this.userDetailsServices=userDetailsServices;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AdNamePasswordAuthenticationToken auth=(AdNamePasswordAuthenticationToken) authentication;
        UserDetails user=null;
        String mark=MarkAntPathRequestMatcherExtractor.MARK_CACHE.get();
        for (AdUserDetailsService userDetailsService : userDetailsServices) {
            if (userDetailsService.support(mark==null ? "" : mark)) {
                user=userDetailsService.loadUserByUsername(username);
                this.preAuthenticationChecks.check(user);
            }
        }
        MarkAntPathRequestMatcherExtractor.MARK_CACHE.remove();
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AdNamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, AdNamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials()==null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new AdUsernamePasswordException("密码错误");
        } else {
            String presentedPassword=authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new AdUsernamePasswordException("密码错误");
            }
        }
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
