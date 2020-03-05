package com.ad.admain.screen;

import java.util.concurrent.Future;

/**
 * @author wezhyn
 * @since 03.04.2020
 */
public interface IdGenerator {


    /**
     * 生成一个当前机器启动时间内唯一 Id
     *
     * @return id
     */
    Future<Long> generate();
}
