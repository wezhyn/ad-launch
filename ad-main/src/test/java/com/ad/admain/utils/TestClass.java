package com.ad.admain.utils;

import java.lang.reflect.Constructor;

/**
 * @author wezhyn
 * @since 12.02.2019
 */
public class TestClass {

    private Integer name;
    private String age;

    public TestClass() {
    }

    public TestClass(String age) {
        this.age=age;
    }

    TestClass(Integer name) {
        this.name=name;
    }

    public static void main(String... args) {
        Constructor[] cons=TestClass.class.getDeclaredConstructors();
        for (Constructor con : cons) {
            System.out.println(con + ":" + con.getModifiers());
        }
    }
}
