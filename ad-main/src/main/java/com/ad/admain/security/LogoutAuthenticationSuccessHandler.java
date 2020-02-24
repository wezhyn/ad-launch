package com.ad.admain.security;

import com.ad.admain.security.jwt.JwtDetailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author : wezhyn
 * @date : 2019/09/20
 */
public class LogoutAuthenticationSuccessHandler implements LogoutSuccessHandler {

    private JwtDetailService jwtDetailService;

    public LogoutAuthenticationSuccessHandler(JwtDetailService jwtDetailService) {
        this.jwtDetailService=jwtDetailService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String result="{\n" +
                "    \"code\": 20000,\n" +
                " \"message\": \"退出成功 \" " +
                "}";
        try (PrintWriter printWriter=response.getWriter()) {
            Integer id=((AdAuthentication) authentication).getId();
            jwtDetailService.deleteSecretByUsername(id);
            printWriter.write(result);
            response.flushBuffer();
            SecurityContext context=SecurityContextHolder.getContext();
            context.setAuthentication(null);
        }
    }
}
