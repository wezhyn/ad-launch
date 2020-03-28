package com.ad.screen.client;

import com.ad.screen.client.vo.req.CompleteNotificationMsg;
import com.ad.screen.client.vo.req.ConfirmMsg;
import com.ad.screen.client.vo.resp.AdEntry;
import com.ad.screen.client.vo.resp.AdScreenResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class AdScreenHanlder extends SimpleChannelInboundHandler<AdScreenResponse> {

    private final HashMap<Integer, AdEntry> cache=new HashMap<>(32);
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
        final AdEntry entry=adScreenResponse.getNetData();
        cache.put(entry.getEntryId()%25, entry);
        log.info("{} 接收到 {}", name, adScreenResponse);
    }


    private static class ConsumerClass implements Runnable {
        private final HashMap<Integer, AdEntry> cache;
        private ChannelHandlerContext context;

        public ConsumerClass(HashMap<Integer, AdEntry> cache, ChannelHandlerContext context) {
            this.cache=cache;
            this.context=context;
        }


        @SneakyThrows
        @Override
        public void run() {
            int count=1;
            while (true) {
                try {
                    String name=context.channel().attr(ScreenClient.REGISTERED_ID).get();
                    final AdEntry entry=cache.get(getIndex(count++));
                    Integer repeatNum=entry.getRepeatNum();
                    entry.setRepeatNum(--repeatNum);
                    log.error("consumer  ： {} at {} ", entry, LocalDateTime.now());
                    if (repeatNum==0) {
                        context.writeAndFlush(new CompleteNotificationMsg(name, entry.getEntryId()));
                    }
                } catch (Exception ignore) {
                    log.info("consumer  ： {} 无 ", count);
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }

        public int getIndex(int count) {
            int mod=count%25;
            return mod==0 ? 25 : mod;
        }
    }
}
