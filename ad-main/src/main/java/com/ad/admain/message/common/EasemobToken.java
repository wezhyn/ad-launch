package com.ad.admain.message.common;

import com.google.gson.Gson;
import io.swagger.client.api.AuthenticationApi;
import io.swagger.client.model.Token;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author wezhyn
 * @date 2019/09/25:19:09
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
public class EasemobToken {

    private final static AuthenticationApi API=new AuthenticationApi();
    private volatile static FutureTask<EasemobToken> CACHE=null;
    private String token;
    private Double expiredAt;

    private EasemobToken(String token, Double expiredAt) {
        this.token=token;
        this.expiredAt=expiredAt;
    }

    public static EasemobToken forAccessToken(EasemobProperties easemobProperties) {
        try {
            if (CACHE==null || CACHE.get().isExpired()) {
                synchronized (EasemobToken.class) {
                    if (CACHE==null || CACHE.get().isExpired()) {
                        CACHE=new FutureTask<>(()->{
                            Token body=new Token().clientId(easemobProperties.getClientId())
                                    .grantType(easemobProperties.getGrantType())
                                    .clientSecret(easemobProperties.getClientSecret());
                            String resp=API.orgNameAppNameTokenPost(easemobProperties.getOrgName(), easemobProperties.getAppName(), body);
                            Gson gson=new Gson();
                            Map map=gson.fromJson(resp, Map.class);
                            String accessToken=" Bearer " + map.get("access_token");
                            Double expiredAt=System.currentTimeMillis() + (Double) map.get("expires_in");
                            return new EasemobToken(accessToken, expiredAt);
                        });
                        CACHE.run();
                    }
                }
            }
            return CACHE.get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("无法获取环信Token");
        }
    }

    /**
     * 是否过期
     *
     * @return true: 过期
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expiredAt;
    }

    public String getToken() {
        return token;
    }


}
