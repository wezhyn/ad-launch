package com.ad.launch.user;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
public interface AdGrantedAuthority {

    /**
     * 获取对应权限
     *
     * @return ROLE_XXX
     */
    String getAuthority();
}
