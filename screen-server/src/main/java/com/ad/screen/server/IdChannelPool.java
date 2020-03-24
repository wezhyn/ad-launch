package com.ad.screen.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.*;

import static com.ad.screen.server.server.ScreenChannelInitializer.REGISTERED_ID;

/**
 * @author wezhyn
 * @since 03.04.2020
 */
@Component
@ChannelHandler.Sharable
public class IdChannelPool {


    private IdGenerator idGenerator;
    private ConcurrentMap<Long, Channel> channelCache;
    /**
     * 系统重启后，需要扫描未完成任务，将已分配的订单的id重新注入
     */
    private BlockingQueue<Long> recycleId;


    @Autowired
    public IdChannelPool() {
        this.idGenerator=new FlakeIdGenerator();
        channelCache=new ConcurrentHashMap<>(2048);
        recycleId=new LinkedBlockingQueue<>();
    }

    public IdChannelPool(Collection<Long> cycleIdPool) {
        recycleId=new LinkedBlockingQueue<>(cycleIdPool);
        channelCache=new ConcurrentHashMap<>(2048);
        this.idGenerator=new FlakeIdGenerator();
    }

    public Long registerChannel(Channel channel) {
        final Long id=getId();
        final Channel pre=channelCache.putIfAbsent(id, channel);
        if (pre!=null) {
//            说明 当前生成的Id 与系统奔溃前遗留冲突
            return registerChannel(channel);
        }
        return id;
    }

    public boolean unregisterChannel(Channel channel) {
        final Long id=channel.attr(REGISTERED_ID).get();
        channelCache.remove(id);
        recycleId.offer(id);
        return true;
    }

    public Long getId() {
        final Long cycleId=recycleId.poll();
        if (cycleId==null) {
            final Future<Long> generateId=idGenerator.generate();
            try {
                return generateId.get();
            } catch (InterruptedException|ExecutionException ignore) {
            }
        }
        return cycleId;
    }

    public Channel getChannel(Long id) {
       return channelCache.get(id);
    }

}
