package com.ad.adlaunch.security.jwt;

import com.ad.adlaunch.constants.JwtProperties;
import com.ad.adlaunch.dto.IUser;
import com.ad.adlaunch.exception.JwtParseException;
import com.ad.adlaunch.security.AdNamePasswordAuthenticationToken;
import com.ad.adlaunch.service.JwtDetailService;
import com.ad.adlaunch.utils.RoleAuthenticationUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

/**
 * 提供 jwt的生成和解析
 * 默认jwt字段：
 *      过期时间
 *      签发时间
 *      id：{@link IUser#getUsername()}
 *      auth: 权限信息
 *
 * @author : wezhyn
 * @date : 2019/09/20
 */
public class SecurityJwtProvider {
    private final JwtSigningKeyResolverAdapter signingKeyResolverAdapter;
    private final JwtDetailService jwtDetailService;
    private final JwtProperties jwtProperties;
    private long tokenValidTime;
    private long tokenRememberTime;

    @Autowired
    public SecurityJwtProvider(JwtProperties jwtProperties, JwtDetailService jwtDetailService) {
        this.jwtProperties=jwtProperties;
        this.jwtDetailService=jwtDetailService;
        this.signingKeyResolverAdapter=new JwtSigningKeyResolverAdapter(jwtDetailService);
    }

    /**
     * 构造后设置 token 过期时间：默认为 1小时，
     * 默认 rememberTime：1天
     */
    @PostConstruct
    public void init() {
        this.tokenValidTime=3600*this.jwtProperties.getDefaultHours()*1000;
        this.tokenRememberTime=86400000*this.jwtProperties.getRememberMeDay();
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims=validateToken(jwt);
        String authority=(String) claims.get("auth");
        Collection<? extends GrantedAuthority> grantedAuthorities=RoleAuthenticationUtils.forGrantedAuthorities(authority);
        return new AdNamePasswordAuthenticationToken(claims.getId(), "", grantedAuthorities);
    }

    public void resetSignature(String userName) {
        jwtDetailService.deleteSecretByUsername(userName);
    }

    public String createToken(Authentication authentication, boolean rememberMe, String userName) {
        Key key=createOrGetKey(userName, null);
        Date time=new Date();
        if (rememberMe) {
            time=new Date(time.getTime() + this.tokenRememberTime);
        } else {
            time=new Date(time.getTime() + this.tokenValidTime);
        }
        String authority=RoleAuthenticationUtils.grantedAuthorities2String(authentication.getAuthorities());
        Claims claims=new DefaultClaims();
        claims
                .setExpiration(time)
                .setIssuedAt(new Date())
                .setId(authentication.getName())
                .setIssuer("system");
        return Jwts.builder()
                .claim("auth", authority)
                .addClaims(claims)
                .signWith(key).compact();
    }


    /**
     * 创建 jwt 密钥 Key
     * @param username 账户
     * @param secret 密钥 为空时创建 key
     * @return Key
     */
    private  Key createOrGetKey(String username,String secret) {
        if (secret==null) {
            secret=getRandomString(50);
            jwtDetailService.saveSecretByUsername(username,secret);
        }
        byte[] encodeKey=Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(encodeKey);
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public  Claims validateToken(String jwt) {
        Claims claims=null;
        String errMsg=null;
        boolean isError=false;

        try {
            claims=Jwts.parser()
                    .setSigningKeyResolver(this.signingKeyResolverAdapter)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (MalformedJwtException|SecurityException var6) {
            errMsg="无效的认证信息";
            isError=true;
        } catch (ExpiredJwtException var7) {
            errMsg="认证信息过期";
            isError=true;
        } catch (UnsupportedJwtException var8) {
            errMsg="不支持的认证方式";
            isError=true;
        } catch (IllegalArgumentException var9) {
            errMsg="JWT token compact of handler are invalid.";
            isError=true;
        }
        if (isError) {
            throw new JwtParseException(errMsg);
        } else {
            return claims;
        }
    }
}
