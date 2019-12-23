package com.ad.admain.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
*
* @author : wezhyn
* @date : 2019/09/20
*
*/
@RestController
public class ExceptionController implements ErrorController {
    @RequestMapping({"/error"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult error(HttpServletRequest request) {
        WebRequest webRequest=new ServletWebRequest(request);
        String message=this.getAttribute(webRequest, "javax.servlet.error.message");
        String path=this.getAttribute(webRequest, "javax.servlet.error.request_uri");
        Exception exception=this.getAttribute(webRequest, "javax.servlet.error.exception");
//        int code=this.getAttribute(webRequest, "com.wezhyn.error.code");
        if (StringUtils.isEmpty(message)&&exception!=null) {
            String detailMessage=exception.getCause()==null ? null : exception.getCause().getMessage();
            message=exception.getMessage()==null ? "" : (detailMessage!=null ? detailMessage : exception.getMessage());
        }
        return ResponseResult.forFailureBuilder()
                .withMessage(message)
                .withData("path", path).build();
    }


    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, 0);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
