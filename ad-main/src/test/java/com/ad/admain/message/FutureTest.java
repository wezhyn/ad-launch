package com.ad.admain.message;

import com.ad.admain.message.common.EasemobProperties;
import io.swagger.client.api.AuthenticationApi;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wezhyn
 * @date 2019/09/26
 * <p>
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
@Slf4j
public class FutureTest {

    private volatile static FutureTask<FutureTest> CACHE=null;
    private final static AuthenticationApi API=new AuthenticationApi();

    private String token;
    private Double expiredAt;
    private static AtomicInteger count=new AtomicInteger(0);

    private FutureTest(String token, Double expiredAt) {
        this.token=token;
        this.expiredAt=expiredAt;
    }

    public static FutureTest forAccessToken(EasemobProperties easemobProperties) {
        try {
            if (CACHE==null||CACHE.get().isExpired()) {
                synchronized (FutureTest.class) {
                    if (CACHE==null||CACHE.get().isExpired()) {
                        CACHE=new FutureTask<>(()->{
                            Thread.sleep(500);
                            log.info("+1");
                            return new FutureTest("token" + count.getAndIncrement(), System.currentTimeMillis() + 4d);
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
                log.info(FutureTest.forAccessToken(null).getToken());
            });
        }
        System.out.println("end");
        executorServices.shutdown();
    }

}
