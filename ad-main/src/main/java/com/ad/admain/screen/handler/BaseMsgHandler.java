package com.ad.admain.screen.handler;

import com.ad.admain.controller.equipment.EquipmentService;
import com.ad.admain.controller.equipment.entity.Equipment;
import com.ad.admain.screen.IdChannelPool;
import com.ad.admain.screen.entity.RemoteInfo;
import com.ad.admain.screen.service.RemoteInfoService;
import com.ad.admain.screen.vo.IScreenFrameServer;
import com.ad.admain.utils.RemoteAddressUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.pool.ChannelPool;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.rmi.Remote;

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
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    RemoteInfoService remoteInfoService;
    @Autowired
    IdChannelPool idChannelPool;
    //消息流水号
    private static final AttributeKey<Short> SERIAL_NUMBER = AttributeKey.newInstance("serialNumber");

    /**
     * 递增获取流水号
     * @return
     */
    public short getSerialNumber(Channel channel){
        Attribute<Short> flowIdAttr = channel.attr(SERIAL_NUMBER);
        Short flowId = flowIdAttr.get();
        if (flowId == null) {
            flowId = 0;
        } else {
            flowId++;
        }
        flowIdAttr.set(flowId);
        return flowId;
    }

    public void write(ChannelHandlerContext ctx, IScreenFrameServer msg) {
        ctx.writeAndFlush(msg).addListener(future -> {
            if (!future.isSuccess()) {
                log.error("发送失败", future.cause());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught",cause);
        ctx.close();
    }

    @Override
    //规定时间内未收到心跳帧则断开连接并将该设备的状态设置为未在线的状态
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //需要在这里判断是否有未处理的任务   直接在ctx里存放两个hashmap 一个存放每个条目编号的对应在消息队列中的
            //id  一个用于存放随着任务完成帧提交时每一个人物的完成状态



            //从id-channel池中删除掉这个channel
            idChannelPool.unregisterChannel(ctx.channel());
            //更新设备的在线状态
            Equipment equipment = (Equipment) ctx.channel().attr(AttributeKey.valueOf("equip")).get();
            equipment.setStatus(false);
            equipmentService.save(equipment);

            //此实例项目只设置了全部超时时间,可以通过state分别做处理,客户端发送心跳维持连接
            IdleState state = ((IdleStateEvent) evt).state();
            //关闭连接
            if (state == IdleState.READER_IDLE) {
                log.warn("客户端{}读取超时,关闭连接", ctx.channel().remoteAddress());
                ctx.close();
            } else if (state == IdleState.WRITER_IDLE) {
                log.warn("客户端{}写入超时", ctx.channel().remoteAddress());
            }else if(state == IdleState.ALL_IDLE){
                log.warn("客户端{}读取写入超时", ctx.channel().remoteAddress());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
