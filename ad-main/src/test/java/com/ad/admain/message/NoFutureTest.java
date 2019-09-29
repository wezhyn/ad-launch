package com.ad.admain.message;

import com.ad.admain.message.common.EasemobProperties;
import io.swagger.client.api.AuthenticationApi;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wezhyn
 * @date 2019/09/26
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class NoFutureTest {

    private volatile static NoFutureTest CACHE=null;
    private final static AuthenticationApi API=new AuthenticationApi();

    private String token;
    private Double expiredAt;
    private static AtomicInteger count=new AtomicInteger(0);

    private NoFutureTest(String token, Double expiredAt) {
        this.token=token;
        this.expiredAt=expiredAt;
    }

    public static NoFutureTest forAccessToken(EasemobProperties easemobProperties) throws InterruptedException {
        if (CACHE==null || CACHE.isExpired()) {
            synchronized (NoFutureTest.class) {
                if (CACHE==null || CACHE.isExpired()) {
                    log.info("+1");
                    Thread.sleep(500);
                    CACHE=new NoFutureTest("token" + count.getAndIncrement(),
                            System.currentTimeMillis() + 4d);
                }
            }
        }
        return CACHE;
    }

    /**
     * 是否过期
     *
     * @return true: 过期
     */
    public boolean isExpired() {
        if (System.currentTimeMillis() > expiredAt) {
            log.info("过期");
            return true;
        }
        return false;
    }

    public String getToken() {
        return token;
    }


    public static void main(String[] args) {
        ExecutorService executorServices=Executors.newCachedThreadPool();
        for (int i=0; i < 1000; i++) {
            executorServices.submit(()->{
                try {
                    log.info(NoFutureTest.forAccessToken(null).getToken());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println("end");
        executorServices.shutdown();
    }

}
