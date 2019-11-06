package com.ad.admain.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.security.Key;

/**
 * 首先从 jwt头部中获取当前用户信息，再通过
 * {@link JwtDetailService#loadSecretByUsername(String)}
 * 加载密钥
 *
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
//        当前 id 必须为 {Iuser@getId}
        Integer id=Integer.valueOf(claims.getId());
        String secret=jwtDetailService.loadSecretById(id);
        if (StringUtils.isEmpty(secret)) {
            throw new UsernameNotFoundException("无法找到对应账户信息");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


}
