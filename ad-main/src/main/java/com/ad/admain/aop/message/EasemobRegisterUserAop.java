package com.ad.admain.aop.message;

import com.ad.admain.message.EasemobService;
import com.ad.admain.to.EasemobUser;
import com.ad.admain.to.GenericUser;
import com.ad.admain.to.IUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author wezhyn
 * @date 2019/09/27
 * <p>
 * 用于在用户注册后，向环信注册用户
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
/*
@Aspect
@Component
*/
public class EasemobRegisterUserAop {

    private final EasemobService easemobService;

    public EasemobRegisterUserAop(EasemobService easemobService) {
        this.easemobService=easemobService;
    }

    @Pointcut("execution(public * com.ad.admain.controller.account.impl.GenericUserServiceImpl.save(..))" +
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
     */
    @Around(value="registerEasemobUser(user)", argNames="joinPoint,user")
    public Object easemobUserRegister(ProceedingJoinPoint joinPoint,
                                      GenericUser user) throws Throwable {

        String username=user.getUsername();
        if (StringUtils.isEmpty(username)) {
            return Optional.empty();
        }
        EasemobUser easemobUser=EasemobUser.builder()
                .easemobId(user.getId())
                .nickname(user.getUsername())
                .password(user.getPassword()).build();
        Optional<EasemobUser> savedUser=easemobService.registerEasemob(easemobUser);
        if (savedUser.isPresent()) {
            return joinPoint.proceed();
        } else {
            return Optional.empty();
        }
    }
}
