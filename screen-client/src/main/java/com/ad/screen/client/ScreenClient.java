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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.AttributeKey;
import javafx.geometry.Point2D;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class ScreenClient {

    public static final AttributeKey<String> REGISTERED_ID=AttributeKey.valueOf("equipment");

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup client=new NioEventLoopGroup();
//        String address="47.111.185.61";
        String address="127.0.0.1";
//        int port=31975;
        int port=8888;
        String equipName="1111111111111" + AdStringUtils.getNum(new Random().nextInt(99), 2);
        log.info("当前设备 IEMI： {}", equipName);
        try {
            Bootstrap b=new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(client)
                    .remoteAddress(address, port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().channel().attr(REGISTERED_ID).set(equipName);
                            socketChannel.pipeline()
                                    .addLast(new LineBasedFrameDecoder(100, true, true))
                                    .addLast(new ScreenProtocolCheckInboundHandler(27, 3, 4, true))
                                    .addLast(new ScreenProtocolOutEncoder())
//                                    .addLast(new IdleStateHandler(120, 170, 180))
                                    .addLast(new ConfirmHandler())
                                    .addLast(new AdScreenHanlder());
                            socketChannel.eventLoop().schedule(()->{
                                socketChannel.pipeline().writeAndFlush(new HeartBeatMsg(equipName));
                                GpsMsg msg=simulateGps(equipName);
                                socketChannel.pipeline().writeAndFlush(msg);
                            }, 0, TimeUnit.SECONDS);
                            socketChannel.eventLoop().scheduleAtFixedRate(()->{
                                socketChannel.pipeline().writeAndFlush(new HeartBeatMsg(equipName));
                            }, 0, 1, TimeUnit.MINUTES);
                            socketChannel.eventLoop().scheduleAtFixedRate(()->{
                                socketChannel.pipeline().writeAndFlush(simulateGps(equipName));
                            }, 0, 2, TimeUnit.MINUTES);
                        }
                    });
            ChannelFuture f=b.connect().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            client.shutdownGracefully().sync();
        }

    }

    private static GpsMsg simulateGps(String equip) {
        Random r=new Random();
        DecimalFormat df=new DecimalFormat("0.00000");
        double x=Double.parseDouble(df.format(12000.85115 + r.nextInt(10)*0.0001d));
        double y=Double.parseDouble(df.format(3022.36405 + r.nextInt(10)*0.0001d));
        return new GpsMsg(new Point2D(x, y), equip);
    }
}
