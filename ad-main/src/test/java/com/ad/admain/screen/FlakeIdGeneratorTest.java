package com.ad.admain.screen;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author wezhyn
 * @since 03.05.2020
 */
public class FlakeIdGeneratorTest {


    @Test
    public void snowFlake() throws InterruptedException {
        final ExecutorService executorService=Executors.newFixedThreadPool(20);
        SnowFlake generator=new SnowFlake(2, 3);
        int count=10000000;
        CountDownLatch latch=new CountDownLatch(count);
        long start=System.currentTimeMillis();
        for (int i=0; i < count; i++) {
            executorService.submit(()->{
                generator.nextId();
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void generate() throws InterruptedException {
        final ExecutorService executorService=Executors.newFixedThreadPool(20);
        FlakeIdGenerator generator=new FlakeIdGenerator();
        int count=10000000;
        CountDownLatch latch=new CountDownLatch(count);
        long start=System.currentTimeMillis();
        for (int i=0; i < count; i++) {
            final Future<Long> generate=generator.generate();
            executorService.submit(()->{
                try {
                    generate.get();
//                    System.out.println(generate.get());
                } catch (InterruptedException|ExecutionException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(System.currentTimeMillis() - start);
    }
}