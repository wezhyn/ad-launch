package com.ad.admain.screen;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成 Id 格式如下 0 ，0000 - 0000(ip 40位) , 0000000(threadNum) ,sequence
 *
 * @author wezhyn
 * @since 03.04.2020
 */
public class FlakeIdGenerator implements IdGenerator {

    /**
     * 每一部分占用的位数,分别为序列号，线程数，ip，0
     */
    private final static long SEQUENCE_BIT=16;
    private final static long THREAD_BIT=7;
    private final static long IP_BIT=40;
    private static final AtomicInteger INDEX=new AtomicInteger(0);
    private long ip=10123255255L;
    private long ipOffset=ip << (SEQUENCE_BIT + THREAD_BIT);
    private ExecutorService idGenerateExecutor;


    public FlakeIdGenerator() {
        this.idGenerateExecutor=new ThreadPoolExecutor((int) THREAD_BIT, (int) THREAD_BIT, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10000), IdTaskThread::new, new ThreadPoolExecutor.CallerRunsPolicy());

    }

    @Override
    public Future<Long> generate() {
        Callable<Long> idTask=()->{
            int sequence, threadId;
            if (Thread.currentThread() instanceof IdTaskThread) {
                IdTaskThread taskThread=(IdTaskThread) Thread.currentThread();
                sequence=taskThread.getAndIncrease();
                threadId=taskThread.getIndex();
            } else {
                sequence=INDEX.getAndIncrement();
                threadId=IdTaskThread.getMaxId();
            }
            return ipOffset|threadId << SEQUENCE_BIT|sequence;
        };
        return idGenerateExecutor.submit(idTask);
    }

    private static class IdTaskThread extends Thread {
        private static int threadSeqIndex;
        private static volatile int maxSeqIndex;
        private static volatile long maxSeqIndexOffset;
        private int idIndex;
        private int tid;


        public IdTaskThread(Runnable target) {
            super(target, "ID_GENERATE_TASK");
            idIndex=0;
            tid=nextIndex();
        }


        public static int getMaxId() {
            int max=maxSeqIndex;
            if (max==0) {
                synchronized (IdTaskThread.class) {
                    if (maxSeqIndex==0) {
                        max=maxSeqIndex=threadSeqIndex++;
                    }
                }
            }
            return max;
        }

        public static int nextIndex() {
            return threadSeqIndex++;
        }

        public int getIndex() {
            return tid;
        }

        public int getAndIncrease() {
            return idIndex++;
        }
    }


}
