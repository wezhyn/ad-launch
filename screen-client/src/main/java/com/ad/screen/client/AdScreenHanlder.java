package com.ad.screen.client;

import com.ad.screen.client.vo.req.CompleteNotificationMsg;
import com.ad.screen.client.vo.req.ConfirmMsg;
import com.ad.screen.client.vo.resp.AdEntry;
import com.ad.screen.client.vo.resp.AdScreenResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class AdScreenHanlder extends SimpleChannelInboundHandler<AdScreenResponse> {

    private final HashMap<Integer, EntryWarp> cache=new HashMap<>(32);
    private final AtomicBoolean isStart=new AtomicBoolean(false);
    private AtomicLong complete;

    public AdScreenHanlder(AtomicLong complete) {
        this.complete=complete;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AdScreenResponse adScreenResponse) throws Exception {
        String name=channelHandlerContext.channel().attr(ScreenClient.REGISTERED_ID).get();
        channelHandlerContext.writeAndFlush(new ConfirmMsg(name));

        if (!isStart.get()) {
            if (isStart.compareAndSet(false, true)) {
                new Thread(new ConsumerClass(cache, channelHandlerContext, complete)).start();
            }
        }
        final AdEntry entry=adScreenResponse.getNetData();
        cache.put(entry.getEntryId()%25, new EntryWarp(entry, entry.getRepeatNum()));
        if (new Random().nextInt(10)==0) {
//            随机中断channel
            log.warn("{} 通道关闭", name);
            channelHandlerContext.channel().close().sync();
        }
        log.info("{} 接收到 {}", name, adScreenResponse);
    }


    private static class ConsumerClass implements Runnable {
        private final HashMap<Integer, EntryWarp> cache;
        private ChannelHandlerContext context;
        private AtomicLong countStatistic;

        public ConsumerClass(HashMap<Integer, EntryWarp> cache, ChannelHandlerContext context, AtomicLong count) {
            this.cache=cache;
            this.context=context;
            this.countStatistic=count;
        }

        @SneakyThrows
        @Override
        public void run() {
            int count=1;
            while (true) {
                try {
                    String name=context.channel().attr(ScreenClient.REGISTERED_ID).get();
                    int index=getIndex(count++);
                    final EntryWarp entryWarp=cache.get(index);
                    AdEntry entry=entryWarp.getAdEntry();
                    Integer repeatNum=entry.getRepeatNum();
                    entry.setRepeatNum(--repeatNum);
                    if (repeatNum==0) {
                        context.writeAndFlush(new CompleteNotificationMsg(name, entry.getEntryId()));
                        context.writeAndFlush(new CompleteNotificationMsg(name, entry.getEntryId()));
                        context.writeAndFlush(new CompleteNotificationMsg(name, entry.getEntryId()));
                        log.error("consumer  ： {}  ", entry);
                        countStatistic.getAndAdd(entryWarp.getInitRepeatNum());
                        cache.remove(index);
                    }
                } catch (Exception ignore) {
                } finally {
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        }

        public int getIndex(int count) {
            int mod=count%25;
            return mod==0 ? 25 : mod;
        }
    }

    @Data
    @AllArgsConstructor
    private static class EntryWarp {
        private AdEntry adEntry;
        private int initRepeatNum;
    }
}
