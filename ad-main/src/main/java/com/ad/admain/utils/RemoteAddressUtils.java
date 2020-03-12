package com.ad.admain.utils;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * @ClassName RemoteAddressUtils
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/24 19:26
 * @Version 1.0
 */
public class RemoteAddressUtils {
    public static String getIp(ChannelHandlerContext channelHandlerContext) {
        InetSocketAddress ipSocket=(InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        String clientIp=ipSocket.getAddress().getHostAddress();
        return clientIp;
    }

    public static int getPort(ChannelHandlerContext channelHandlerContext) {
        InetSocketAddress ipSocket=(InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        int clientIp=ipSocket.getPort();
        return clientIp;
    }
}
