package com.ad.screen.server.event;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @author wezhyn
 * @since 05.27.2020
 */
@Slf4j
public final class LocalResumeState {

    public static final LocalResumeState INSTANCE = new LocalResumeState();
    public static Supplier<Integer> ZERO_RESUME_INDEX = () -> 0;
    private int count;

    public synchronized int getResumeIndex() {
        return count;
    }

    public synchronized void setResumeIndex(int count) {
        this.count = count;
    }


    public synchronized void resetResumeIndex(Supplier<Integer> resumeIndex) {
        Integer index;
        if (resumeIndex == null || (index = resumeIndex.get()) == null) {
            index = 0;
            log.warn("异常重置本地服务至: {}", index);

        }
        this.count = index;
    }

    public synchronized void resetResumeIndex() {
        resetResumeIndex(ZERO_RESUME_INDEX);
    }
}
