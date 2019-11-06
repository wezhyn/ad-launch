package com.ad.admain.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.AuthenticatedPrincipal;

import javax.annotation.concurrent.Immutable;

/**
 * @author wezhyn
 * @date 2019/11/06
 * <p>
 * 储存在 {@link AdAuthentication} 中的信息
 * 包含 id， username
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@AllArgsConstructor
@Immutable
public class AdIdPrincipal implements AuthenticatedPrincipal {
    @Getter
    private final Integer id;
    private final String username;

    @Override
    public String getName() {
        return username;
    }
}
