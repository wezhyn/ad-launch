package com.ad.admain.controller;

import com.wezhyn.project.controller.ResponseResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 */
@RestController
@ControllerAdvice
public class ExceptionController implements ErrorController {

    @ExceptionHandler(value={AccessDeniedException.class})
    @ResponseStatus(code=HttpStatus.OK)
    public ResponseResult accessDeniedHandler(Exception e, HttpServletRequest request) {
        String path=request.getServletPath();
        return ResponseResult.forHttpStatusCode(HttpStatus.FORBIDDEN.value())
                .withPath(path)
                .withMessage(e.getMessage())
                .build();
    }


    @RequestMapping({"/error"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult error(HttpServletRequest request) {
        WebRequest webRequest=new ServletWebRequest(request);
        String message=this.getAttribute(webRequest, "javax.servlet.error.message");
        Integer status=this.getAttribute(webRequest, "javax.servlet.error.status_code");
        String path=this.getAttribute(webRequest, "javax.servlet.error.request_uri");
        Exception exception=this.getAttribute(webRequest, "javax.servlet.error.exception");
        if (StringUtils.isEmpty(message) && exception!=null) {
            String detailMessage=exception.getCause()==null ? null : exception.getCause().getMessage();
            message=exception.getMessage()==null ? "" : (detailMessage!=null ? detailMessage : exception.getMessage());
        }
        return ResponseResult.forHttpStatusCode(status)
                .withMessage(message)
                .withPath(path).build();
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
