package com.ad.admain.controller;

import com.ad.admain.controller.exception.SmsException;
import com.alibaba.fastjson.JSONObject;
import com.wezhyn.project.controller.NoNestResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author wezhyn
 * @since 02.22.2020
 */
@RestController
@RequestMapping("/api/code")
public class SmsController {


    private final ISmsService smsService;

    public SmsController(ISmsService smsService) {
        this.smsService=smsService;
    }

    @PostMapping("/register")
    public NoNestResponseResult<String> sendMsg(@RequestBody JSONObject jsonObject) throws SmsException {
        final String mobilePhone=jsonObject.getString("mobilePhone");
        smsService.send(mobilePhone, ISmsService.SmsType.REGISTER);
        return NoNestResponseResult.successResponseResult("创建短信成功", "");
    }


    @ExceptionHandler(value={SmsException.class})
    public NoNestResponseResult<String> error(Exception e) {
        return NoNestResponseResult.failureResponseResult(e.getMessage());
    }

}
