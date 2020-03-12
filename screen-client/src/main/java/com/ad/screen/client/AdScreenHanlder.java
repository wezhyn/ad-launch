package com.ad.screen.client;

import com.ad.screen.client.vo.req.CompleteNotificationMsg;
import com.ad.screen.client.vo.req.ConfirmMsg;
import com.ad.screen.client.vo.resp.AdEntry;
import com.ad.screen.client.vo.resp.AdScreenResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class AdScreenHanlder extends SimpleChannelInboundHandler<AdScreenResponse> {

    private final ConcurrentHashMap<Integer, AdEntry> cache=new ConcurrentHashMap<>();
    private final AtomicBoolean isStart=new AtomicBoolean(false);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AdScreenResponse adScreenResponse) throws Exception {
        String name=channelHandlerContext.channel().attr(ScreenClient.REGISTERED_ID).get();
        channelHandlerContext.writeAndFlush(new ConfirmMsg(name));
        if (!isStart.get()) {
            if (isStart.compareAndSet(false, true)) {
                new Thread(new ConsumerClass(cache, channelHandlerContext)).start();
            }
        }
        cache.put(adScreenResponse.getNetData().getEntryId(), adScreenResponse.getNetData());
        log.info("{} 接收到 {}", name, adScreenResponse);
    }


    private static class ConsumerClass implements Runnable {
        private final ConcurrentHashMap<Integer, AdEntry> cache;
        private ChannelHandlerContext context;
        private int current;

        public ConsumerClass(ConcurrentHashMap<Integer, AdEntry> cache, ChannelHandlerContext context) {
            this.cache=cache;
            this.context=context;
            current=0;
        }


        @Override
        public void run() {

            String name=context.channel().attr(ScreenClient.REGISTERED_ID).get();
            AdEntry entry=null;
            while (entry==null) {
                entry=cache.get(current++);
            }
            log.info("consumer {} at {} ", entry, LocalDateTime.now());
            context.writeAndFlush(new CompleteNotificationMsg(name, entry.getEntryId()));
        }
    }
}
