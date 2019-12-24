package com.ad.admain.security.filter;

import com.ad.admain.config.web.JwtProperties;
import com.ad.admain.security.IJwtRequestRead;
import com.ad.admain.security.jwt.SecurityJwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查看请求中是否携带 jwt 信息
 * 若有则提取出 {@link Authentication}
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
@Slf4j
public class AdJwtCheckAuthenticationFilter extends GenericFilterBean {
    private SecurityJwtProvider securityJwtProvider;
    private List<RequestMatcher> filterExclusion;
    private List<IJwtRequestRead> jwtRequestReads;
    private String checkHeader;

    public AdJwtCheckAuthenticationFilter(SecurityJwtProvider securityJwtProvider, JwtProperties jwtProperties, List<IJwtRequestRead> jwtRequestReads) {
        this.securityJwtProvider=securityJwtProvider;
        this.filterExclusion=new ArrayList<>(3);
        this.checkHeader=jwtProperties.getCheckHeader();
        this.jwtRequestReads=jwtRequestReads;
        jwtProperties.getCheckListExclusion().forEach(s->this.filterExclusion.add(new AntPathRequestMatcher(s)));
        if (log.isDebugEnabled()) {
            log.debug("不拦截： {}", jwtProperties.getCheckListExclusion().toString());
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest) request;
        SecurityContext securityContext=SecurityContextHolder.getContext();
        //     当当前无权限时，尝试从jwt中获取Authentication
        if (!matches(request) && securityContext.getAuthentication()==null) {
            String jwt="";
            for (IJwtRequestRead re : jwtRequestReads) {
                String s=re.readJwt((HttpServletRequest) request);
                if (s!=null) {
                    jwt=s;
                    break;
                }
            }
            if (!StringUtils.isEmpty(jwt)) {
                Authentication authentication=securityJwtProvider.getAuthentication(jwt);
                securityContext.setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
        securityContext.setAuthentication(null);
    }

    private boolean matches(ServletRequest req) {
        HttpServletRequest request=(HttpServletRequest) req;
        long count=this.filterExclusion.stream()
                .filter(requestMatcher->requestMatcher.matches(request))
                .count();
        return count > 0L;
    }
}
