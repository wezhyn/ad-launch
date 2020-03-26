package com.ad.screen.server.handler;

import com.ad.screen.server.FailTaskService;
import com.ad.screen.server.IdChannelPool;
import com.ad.screen.server.cache.PooledIdAndEquipCacheService;
import com.ad.screen.server.vo.IScreenFrameServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName BaseHandler
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 1:51
 * @Version 1.0
 */
@Slf4j

public abstract class BaseMsgHandler<T> extends SimpleChannelInboundHandler<T> {
    //消息流水号
    private static final AttributeKey<Short> SERIAL_NUMBER=AttributeKey.newInstance("serialNumber");
    @Autowired
    IdChannelPool idChannelPool;
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Autowired
    FailTaskService failTaskService;
    @Autowired
    PooledIdAndEquipCacheService pooledIdAndEquipCacheService;


    /**
     * 递增获取流水号
     *
     * @return short
     */
    public short getSerialNumber(Channel channel) {
        Attribute<Short> flowIdAttr=channel.attr(SERIAL_NUMBER);
        Short flowId=flowIdAttr.get();
        if (flowId==null) {
            flowId=0;
        } else {
            flowId++;
        }
        flowIdAttr.set(flowId);
        return flowId;
    }

    public void write(ChannelHandlerContext ctx, IScreenFrameServer msg) {
        ctx.writeAndFlush(msg).addListener(future->{
            if (!future.isSuccess()) {
                log.error("发送失败", future.cause());
            }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }


}
