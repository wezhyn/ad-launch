package com.ad.admain.security;

import com.ad.admain.constants.JwtProperties;
import com.ad.admain.utils.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

/**
 * @author : wezhyn
 * @date : 2019/09/24
 * <p>
 * 缓存登录时通过 username 查询的用户,在{@link com.ad.admain.security.filter.AdUsernamePasswordAuthenticationProvider}
 * 中使用，并清除
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public abstract class AdUserDetailsService implements UserDetailsService {


    /**
     * 根据 username 查找用户信息，在返回的userDetails 中添加自增主键
     *
     * @param username username
     * @return id, username, password,authentication
     * @throws UsernameNotFoundException 无该用户信息
     */
    @Override
    public final AdUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("无效的账户： " + username + "，请检查是否为空");
        }

        return getUserDetails(username);
    }

    /**
     * mark: 为{@link JwtProperties#getLoginInterceptionInclude()}中的key
     *
     * @param mark 拦截标识
     * @return true
     */
    public abstract boolean support(String mark);

    protected abstract AdUserDetails getUserDetails(String username);


    public static class AdUser extends User implements AdUserDetails {
        private Integer id;

        public AdUser(Integer id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            this.id=id;
        }

        public AdUser(Integer id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
            this.id=id;
        }

        @Override
        public Integer getId() {
            return id;
        }
    }
}
