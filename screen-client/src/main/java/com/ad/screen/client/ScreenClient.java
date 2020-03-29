package com.ad.screen.client;

import com.ad.launch.order.TaskMessage;
import com.ad.screen.client.vo.req.GpsMsg;
import com.ad.screen.client.vo.req.HeartBeatMsg;
import com.google.gson.Gson;
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
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wezhyn
 * @since 03.12.2020
 */
@Slf4j
public class ScreenClient {

    public static final AttributeKey<String> REGISTERED_ID=AttributeKey.valueOf("equipment");

    public static void main(String[] args) throws InterruptedException {
        String address="47.111.185.61";
        int port=31975;
        Integer begin=null;
        AtomicLong count=new AtomicLong(0);
        for (String arg : args) {
            try {
                begin=Integer.valueOf(arg);
            } catch (Exception ignore) {
            }
        }
        if (begin==null) {
            log.error("输入启动开始设备数");
            return;
        }
        ExecutorService service=Executors.newCachedThreadPool();
        for (AtomicInteger i=new AtomicInteger(1001); i.get() < 1005; ) {
            service.submit(()->{
                runOne(address, port, createEquipName(i.getAndIncrement()), count);
            });
        }
        service.submit(()->{
            DefaultMQProducer producer=new DefaultMQProducer("test-order-message");
            // 设置NameServer的地址
            producer.setNamesrvAddr("47.111.185.61:9876");
            producer.setVipChannelEnabled(false);
            int orderUid=0;
//            while (true) {
            orderUid=(++orderUid)%1000;
            Random r=new Random();
            int rate=r.nextInt(20) + 1;
            int dn=r.nextInt(5);
            TaskMessage taskMessage=TaskMessage.builder()
                    .deliverNum(dn)
                    .latitude(30.2000)
                    .longitude(120.0000)
                    .oid(orderUid)
                    .produceContext(Collections.singletonList("test-" + orderUid))
                    .rate(rate)
                    .vertical(true)
                    .uid(orderUid)
                    .scope(1000D)
                    .totalNum(dn*rate)
                    .build();
            Gson gson=new Gson();
            Message message=new Message("task_message_topic", "*", gson.toJson(taskMessage).getBytes());

            try {
                producer.start();
                producer.sendOneway(message);
            } catch (MQClientException|RemotingException|InterruptedException e) {
                e.printStackTrace();
            }
//            }
        });
        TimeUnit.HOURS.sleep(1);
    }


    private static String createEquipName(Integer count) {
        String c=count.toString();
        StringBuilder sb=new StringBuilder();
        for (int i=0; i < 15 - c.length(); i++) {
            sb.append("0");
        }
        sb.append(c);
        return sb.toString();
    }

    private static void runOne(String address, int port, String equipName, AtomicLong count) {
        EventLoopGroup client=new NioEventLoopGroup();
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
                                    .addLast(new AdScreenHanlder(count));
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
            client.shutdownGracefully();
        } finally {
            try {
                client.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static GpsMsg simulateGps(String equip) {
        Random r=new Random();
        DecimalFormat df=new DecimalFormat("0.00000");
        double x=Double.parseDouble(df.format(12000.00000 + r.nextInt(10)*0.0001d));
        double y=Double.parseDouble(df.format(3020.00000 + r.nextInt(10)*0.0001d));
        return new GpsMsg(new Point2D(x, y), equip);
    }
}
