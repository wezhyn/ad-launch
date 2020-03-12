package com.ad.admain.aop;

import com.ad.admain.controller.ISmsService;
import com.ad.admain.controller.account.RegisterDto;
import com.wezhyn.project.controller.ResponseResult;
import com.wezhyn.project.utils.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
@Aspect
@Component
public class RegisterAop {


    @Autowired
    private ISmsService smsService;


    @Pointcut("execution( public * com.ad.admain.controller.account.NoCheckUserController.register(..))" +
            "&&args(registerDto)")
    public void registerUser(RegisterDto registerDto) {
    }

    @Around(value="registerUser(registerDto)", argNames="pjp,registerDto")
    public Object smsHandle(ProceedingJoinPoint pjp, RegisterDto registerDto) throws Throwable {
        if (registerDto==null) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("无注册信息").build();
        }
        if (Strings.isEmpty(registerDto.getMobilePhone())) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("无手机信息").build();
        }
        if (registerDto.getCode()==null || registerDto.getCode().length()==0) {
            return ResponseResult.forFailureBuilder()
                    .withMessage("无验证码").build();
        }
        final ISmsService.Verify verify=smsService.verifyCode(registerDto.getMobilePhone(), registerDto.getCode(), ISmsService.SmsType.REGISTER);
        if (verify!=ISmsService.Verify.SUCCESS) {
            return ResponseResult.forFailureBuilder()
                    .withMessage(verify.getValue()).build();
        }
        return pjp.proceed();
    }

}
