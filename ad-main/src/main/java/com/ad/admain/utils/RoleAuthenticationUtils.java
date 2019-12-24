package com.ad.admain.utils;

import com.ad.admain.controller.account.AuthenticationEnum;
import com.wezhyn.project.utils.EnumUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 根据 {@link AuthenticationEnum 动态生成对应的权限关系}
 * 为了保证线程安全性，返回的权限集合使用 {@link Collections#unmodifiableCollection}
 * 进行包装
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
public final class RoleAuthenticationUtils {
    /**
     * 实际使用的 {@link SimpleGrantedAuthority} 是不可变的
     */
    public static final Map<AuthenticationEnum, Set<GrantedAuthority>> CACHE;
    private static final String ROLE_PREFIX="ROLE_";

    static {
        CACHE=Stream.of(AuthenticationEnum.values())
                .collect(()->new EnumMap<>(AuthenticationEnum.class), (m, a)->{
//      m:map<AuthenticationEnum,Set<GrantedAuthority>> a:AuthenticationEnum
//                    查找权重比当前 a 权重低的
                    Set<GrantedAuthority> authorities=Stream.of(AuthenticationEnum.values())
                            .filter(a2->a2.getOrdinal() < a.getOrdinal())
                            .map(RoleAuthenticationUtils::authenticationEnum2GrantedAuthority)
                            .collect(Collectors.toSet());
//                    补上当前 a
                    authorities.add(RoleAuthenticationUtils.authenticationEnum2GrantedAuthority(a));
                    m.put(a, authorities);
                }, EnumMap::putAll);
    }

    private RoleAuthenticationUtils() {
    }

    /**
     * 通过{@link AuthenticationEnum} 获取对应的权限
     *
     * @param authenticationEnum authenticationEnum
     * @return collection:has key ;
     */
    public static Collection<? extends GrantedAuthority> forGrantedAuthorities(AuthenticationEnum authenticationEnum) {
        Collection<? extends GrantedAuthority> grantedAuthorities=CACHE.get(authenticationEnum);
        if (grantedAuthorities==null) {
            throw new EnumConstantNotPresentException(AuthenticationEnum.class, authenticationEnum.getValue());
        }
        return Collections.unmodifiableCollection(grantedAuthorities);
    }

    /**
     * 获取 GrantedAuthority
     *
     * @param authentication {@link AuthenticationEnum#getValue()}
     * @return collection:has value;null:no value;
     */
    public static Collection<? extends GrantedAuthority> forGrantedAuthorities(String authentication) {
        AuthenticationEnum authenticationEnum=EnumUtils.valueOfStringEnumIgnoreCase(AuthenticationEnum.class, authentication);
        return forGrantedAuthorities(authenticationEnum);
    }

    /**
     * 返回权限集合中最大的权限
     *
     * @param grantedAuthorities 权限集合 ROLE_XXX
     * @return {@link AuthenticationEnum#getValue()} XXX
     */
    public static String grantedAuthorities2SingleString(Collection<? extends GrantedAuthority> grantedAuthorities) {
        return Stream.of(AuthenticationEnum.values())
//                按照 {getOrdinal }降序
                .sorted(Comparator.comparingInt(AuthenticationEnum::getOrdinal).reversed())
                .filter(a->grantedAuthorities.contains(authenticationEnum2GrantedAuthority(a)))
                .findFirst().orElseThrow(()->new EnumConstantNotPresentException(AuthenticationEnum.class, ""))
                .getValue();
    }

    public static boolean isAuthentication(Collection<GrantedAuthority> authorities) {

        return Stream.of(AuthenticationEnum.values())
//                按照 {getOrdinal }降序
                .sorted(Comparator.comparingInt(AuthenticationEnum::getOrdinal).reversed())
                .filter(a->a.getOrdinal() >= AuthenticationEnum.ADMIN.getOrdinal())
                .anyMatch(a->authorities.contains(authenticationEnum2GrantedAuthority(a)));

    }

    /**
     * 返回 权限的集合[去掉 ROLE_ 前缀]
     *
     * @param authenticationEnum enum
     * @return string[]
     */
    public static String[] authentication2ValueStringList(AuthenticationEnum authenticationEnum) {
        return CACHE.get(authenticationEnum).stream()
                .map(GrantedAuthority::getAuthority)
                .map(RoleAuthenticationUtils::grantedAuthority2AuthenticationEnumValue)
                .toArray(String[]::new);
    }

    /**
     * 只由当前类调用， grantedAuthority就一定以ROLE_开头
     *
     * @param grantedAuthority ROLE_XXX
     * @return XXX
     */
    private static AuthenticationEnum grantedAuthority2AuthenticationEnum(String grantedAuthority) {
        if (grantedAuthority.startsWith(ROLE_PREFIX)) {
            String role=grantedAuthority.substring(ROLE_PREFIX.length());
            return EnumUtils.valueOfStringEnumIgnoreCase(AuthenticationEnum.class, role);
        }
        throw new EnumConstantNotPresentException(AuthenticationEnum.class, grantedAuthority);
    }

    private static String grantedAuthority2AuthenticationEnumValue(String grantedAuthority) {
        return grantedAuthority2AuthenticationEnum(grantedAuthority).getValue();
    }

    private static String authenticationEnum2GrantedAuthorityValue(AuthenticationEnum authenticationEnum) {
        return ROLE_PREFIX + authenticationEnum.getValue().toUpperCase();
    }

    private static SimpleGrantedAuthority authenticationEnum2GrantedAuthority(AuthenticationEnum authenticationEnum) {
        return new SimpleGrantedAuthority(authenticationEnum2GrantedAuthorityValue(authenticationEnum));
    }


}
