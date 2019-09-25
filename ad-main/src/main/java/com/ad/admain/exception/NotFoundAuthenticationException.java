package com.ad.admain.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author : wezhyn
 * @date : 2019/09/19
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class NotFoundAuthenticationException extends UsernameNotFoundException {


    public NotFoundAuthenticationException(String explanation) {
        super(explanation);
    }

}
