package com.ad.launch.user;

import com.ad.launch.user.exception.GrantedAuthorityException;
import com.wezhyn.project.utils.StringUtils;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
public class AdSimpleGrantedAuthority implements AdGrantedAuthority {

    private final String role;

    public AdSimpleGrantedAuthority(String role) {
        if (StringUtils.isEmpty(role)) {
            throw new GrantedAuthorityException("权限参数不应该为空");
        }
        this.role=role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) {
            return true;
        }
        if (obj instanceof AdSimpleGrantedAuthority) {
            return role.equals(((AdSimpleGrantedAuthority) obj).role);
        }

        return false;
    }

    @Override
    public String toString() {
        return this.role;
    }
}
