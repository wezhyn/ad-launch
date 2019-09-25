package com.ad.admain.utils;

import com.ad.admain.enumate.AuthenticationEnum;
import com.ad.admain.exception.NotFoundAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 */
public final class RoleAuthenticationUtils {
    private static final String ROLE_PREFIX="ROLE_";
    public static final Set<SimpleGrantedAuthority> USER_ROLE;
    private static final String USER_ROLE_ATTRIBUTE="ROLE_USER";
    public static final Set<SimpleGrantedAuthority> ADMIN_ROLE;
    private static final String ADMIN_ROLE_ATTRIBUTE="ROLE_ADMIN";

    private RoleAuthenticationUtils() {
    }

    public static Collection<? extends GrantedAuthority> forGrantedAuthorities(AuthenticationEnum authenticationEnum) throws NotFoundAuthenticationException {
        switch (authenticationEnum) {
            case USER:
                return USER_ROLE;
            case ADMIN:
                return ADMIN_ROLE;
            default:
                throw new NotFoundAuthenticationException("运行时错误：无法找到对应权限：" + authenticationEnum.getValue());
        }
    }

    public static Collection<? extends GrantedAuthority> forGrantedAuthorities(String authentication) {
        switch (authentication) {
            case USER_ROLE_ATTRIBUTE:
                return USER_ROLE;
            case ADMIN_ROLE_ATTRIBUTE:
                return ADMIN_ROLE;
            default:
                throw new NotFoundAuthenticationException("运行时错误：无法找到对应权限：" + authentication);
        }
    }

    public static String grantedAuthorities2String(Collection<? extends GrantedAuthority> grantedAuthorities) {
        int flag=0;
        for (GrantedAuthority g : grantedAuthorities) {
            String auth=g.getAuthority();
            if (auth.equalsIgnoreCase(USER_ROLE_ATTRIBUTE)) {
                if (flag == 0) {
                    flag=1;
                }
            } else if (auth.equalsIgnoreCase(ADMIN_ROLE_ATTRIBUTE)) {
                flag=2;
            }
        }
        switch (flag) {
            case 1:return USER_ROLE_ATTRIBUTE;
            case 2:return ADMIN_ROLE_ATTRIBUTE;
            default:{
                return "";
            }
        }
    }

    public static String[] authentication2StringList(AuthenticationEnum authenticationEnum) {
        switch (authenticationEnum) {
            case ADMIN:
                return toStringArray(ADMIN_ROLE);
            case USER:
                return toStringArray(USER_ROLE);
            default:
                return new String[0];
        }
    }

    private static String[] toStringArray(Collection<? extends GrantedAuthority> grantedAuthorities) {
        String[] result=new String[grantedAuthorities.size()];
        int i=0;
        for (GrantedAuthority g : grantedAuthorities) {
            result[i++]=g.getAuthority().substring(ROLE_PREFIX.length()).toLowerCase();
        }
        return result;
    }
    static {
        USER_ROLE=Collections.singleton(new SimpleGrantedAuthority(USER_ROLE_ATTRIBUTE));
        ADMIN_ROLE=new HashSet<>(USER_ROLE);
        ADMIN_ROLE.add(new SimpleGrantedAuthority(ADMIN_ROLE_ATTRIBUTE));
    }
}
