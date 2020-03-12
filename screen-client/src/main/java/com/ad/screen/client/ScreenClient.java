package com.ad.screen.client;

import com.ad.screen.client.vo.req.GpsMsg;
import com.ad.screen.client.vo.req.HeartBeatMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import javafx.geometry.Point2D;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
public class ScreenClient {

    public static final AttributeKey<String> REGISTERED_ID=AttributeKey.valueOf("equipment");

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup client=new NioEventLoopGroup();
        String equipName="1111111111111" + AdStringUtils.getNum(new Random().nextInt(100), 2);
        try {
            Bootstrap b=new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(client)
                    .remoteAddress("localhost", 28888)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().channel().attr(REGISTERED_ID).set(equipName);
                            socketChannel.pipeline().addLast(new ScreenProtocolCheckInboundHandler(27, 3, 4, true))
                                    .addLast(new ScreenProtocolOutEncoder())
                                    .addLast(new AdScreenHanlder());
                            socketChannel.eventLoop().scheduleAtFixedRate(()->{
                                socketChannel.pipeline().writeAndFlush(new HeartBeatMsg(equipName));
                            }, 0, 1, TimeUnit.MINUTES);
                            socketChannel.eventLoop().scheduleAtFixedRate(()->{
                                Random r=new Random();
                                DecimalFormat df=new DecimalFormat("0.00000");
                                double x=Double.parseDouble(df.format(12000.85115 + r.nextInt(10000)*0.0001d));
                                double y=Double.parseDouble(df.format(3013.16405 + r.nextInt(10000)*0.0001d));
                                socketChannel.pipeline().writeAndFlush(new GpsMsg(new Point2D(x, y), equipName));
                            }, 0, 5, TimeUnit.MINUTES);
                        }
                    });
            ChannelFuture f=b.connect().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            client.shutdownGracefully().sync();
        }

    }
}
