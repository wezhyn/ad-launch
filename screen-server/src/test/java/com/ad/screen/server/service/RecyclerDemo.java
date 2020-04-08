package com.ad.screen.server.service;

import io.netty.util.Recycler;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 04.07.2020
 */
public class RecyclerDemo {

    @Test
    public void test() throws InterruptedException {
        Recycler<User> recycler=new Recycler<User>() {
            @Override
            protected User newObject(Handle<User> handle) {
                return new User(handle);
            }
        };
        final User user=recycler.get();
        new Thread(user::recycle).start();
        TimeUnit.HOURS.sleep(1);
    }

    @Test
    public void doTest() {
        double l;
        long s=System.currentTimeMillis();
        for (int i=0; i < 100; i++) {
            l=vmDo();
        }
        long now=System.currentTimeMillis();
        System.out.println("Elapsed time: " + (now - s));
    }

    private long vmDo() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    static class User {
        private String name;
        private Recycler.Handle<User> handle;

        public User(Recycler.Handle<User> handle) {
            this.handle=handle;
        }

        public void recycle() {
            this.handle.recycle(this);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name=name;
        }
    }
}
