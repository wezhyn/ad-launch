package com.ad.admain.screen;

import io.netty.channel.ChannelPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.channels.Channel;
import java.util.Collection;
import java.util.concurrent.*;

/**
 * @author wezhyn
 * @since 03.04.2020
 */
@Component
public class IdChannelPool {


    private IdGenerator idGenerator;
    private ConcurrentMap<Long, ChannelPipeline> channelCache;
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

    public Long registerChannel(ChannelPipeline channel) {
        final Long id=getId();
        final ChannelPipeline pre=channelCache.putIfAbsent(id, channel);
        if (pre!=null) {
//            说明 当前生成的Id 与系统奔溃前遗留冲突
            return registerChannel(channel);
        }
        return id;
    }

    public boolean unregisterChannel(Channel channel) {
//        channelCache.remove()
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


}
