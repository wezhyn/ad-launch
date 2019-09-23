package com.ad.adlaunch.security.jwt;

import com.ad.adlaunch.service.JwtDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.security.Key;

/**
 * 首先从 jwt头部中获取当前用户信息，再通过
 * {@link com.ad.adlaunch.service.JwtDetailService#loadSecretByUsername(String)}
 * 加载密钥
 * @author : wezhyn
 * @date : 2019/09/20
 */
public class JwtSigningKeyResolverAdapter extends SigningKeyResolverAdapter {

    private JwtDetailService jwtDetailService;

    public JwtSigningKeyResolverAdapter(JwtDetailService jwtDetailService) {
        this.jwtDetailService=jwtDetailService;
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
//        当前 id 必须为 {Iuser@getUsername}
        String username=claims.getId();
        String secret=jwtDetailService.loadSecretByUsername(username);
        if (StringUtils.isEmpty(secret)) {
            throw new UsernameNotFoundException("无法找到对应账户信息");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


}
