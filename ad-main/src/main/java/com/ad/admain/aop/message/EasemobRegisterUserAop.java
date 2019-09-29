package com.ad.admain.aop.message;

import com.ad.admain.dto.EasemobUser;
import com.ad.admain.dto.GenericUser;
import com.ad.admain.dto.IUser;
import com.ad.admain.service.EasemobService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * 用于在用户注册后，向环信注册用户
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Aspect
@Component
public class EasemobRegisterUserAop {

    @Autowired
    private EasemobService easemobService;

    @Pointcut("execution(public * com.ad.admain.service.impl.GenericUserServiceImpl.save(..))" +
            "&&args(user)")
    public void registerEasemobUser(GenericUser user) {
    }

    /**
     * 当抛出异常时，不进行具体的注册，返回上层一个空对象
     * 注册的用户名：{@link IUser#getId()}
     * 密码：{@link IUser#getPassword()}
     *
     * @param joinPoint joinPoint
     * @param user      注册的用户
     * @return {@link EasemobUser#EMPTY_EASEMOB}
     */
    @Around(value="registerEasemobUser(user)", argNames="joinPoint,user")
    public Object easemobUserRegister(ProceedingJoinPoint joinPoint,
                                      GenericUser user) throws Throwable {

        String username=user.getUsername();
        if (StringUtils.isEmpty(username)) {
            return GenericUser.EMPTY_USER;
        }
        EasemobUser easemobUser=EasemobUser.builder()
                .easemobId(user.getId())
                .nickname(user.getUsername())
                .password(user.getPassword()).build();
        EasemobUser savedUser=easemobService.registerEasemob(easemobUser);
        if (savedUser==EasemobUser.EMPTY_EASEMOB) {
            return GenericUser.EMPTY_USER;
        } else {
            return joinPoint.proceed();
        }
    }
}
