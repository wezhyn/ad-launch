package com.ad.screen.client;

import com.ad.launch.order.TaskMessage;
import com.ad.screen.client.vo.req.GpsMsg;
import com.ad.screen.client.vo.req.HeartBeatMsg;
import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.AttributeKey;
import javafx.geometry.Point2D;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
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
    private static final Options OPTIONS=new Options();

    static {
        OPTIONS.addOption(Option.builder()
                .argName("smsg")
                .longOpt("smsg")
                .hasArg()
                .desc("发送消息的初始编号")
                .type(Number.class)
                .required()
                .build());
        OPTIONS.addOption(Option.builder()
                .argName("ismsg")
                .longOpt("ismsg")
                .hasArg()
                .desc("是否发送消息")
                .type(Boolean.class)
                .required()
                .build());

        OPTIONS.addOption(Option.builder()
                .argName("emsg")
                .longOpt("emsg")
                .hasArg()
                .desc("发送消息的终止编号")
                .type(Number.class)
                .required()
                .build());
        OPTIONS.addOption(Option.builder()
                .argName("srun")
                .longOpt("srun")
                .hasArg()
                .desc("模拟设备的初始编号")
                .type(Number.class)
                .required()
                .build());
        OPTIONS.addOption(Option.builder()
                .argName("erun")
                .longOpt("erun")
                .hasArg()
                .desc("模拟设备的终止编号")
                .type(Number.class)
                .required()
                .build());
        OPTIONS.addOption(Option.builder()
                .argName("s")
                .longOpt("s")
                .hasArg()
                .desc("远程服务器地址")
                .type(String.class)
                .required()
                .build());

        OPTIONS.addOption(Option.builder()
                .argName("p")
                .longOpt("p")
                .desc("远程服务器端口")
                .hasArg()
                .type(Number.class)
                .required()
                .build());
    }

    public static void main(String[] args) throws InterruptedException, ParseException {
        final CommandLineArg command=createCommand(args);
        ExecutorService service=Executors.newCachedThreadPool();
        AtomicLong count=new AtomicLong(0);
        AtomicInteger runnerCount=new AtomicInteger(command.getStartRunner());
        AtomicInteger taskCount=new AtomicInteger(command.getStartMsg());
        DefaultMQProducer producer=createMq();
        while (true) {
            if (runnerCount.get() < command.getEndRunner()) {
                final int nowCount=runnerCount.getAndIncrement();
                service.submit(()->{
                    runOne(command, createEquipName(nowCount), count);
                });
                TimeUnit.SECONDS.sleep(1);
            } else {
                Thread.yield();
            }
            if (command.isSend && taskCount.get() < command.getEndMsg()) {
                if (new Random().nextInt(20)==0) {
                    final int nowCount=taskCount.getAndIncrement();
                    service.submit(()->sendMessage(nowCount, producer));
                }
            } else {
                Thread.yield();
            }
        }
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

    private static void sendMessage(int orderNum, DefaultMQProducer producer) {
        Random r=new Random();
        int rate=r.nextInt(5) + 1;
        int dn=r.nextInt(5) + 1;
        TaskMessage taskMessage=TaskMessage.builder()
                .deliverNum(dn)
                .latitude(30.2000)
                .longitude(120.0000)
                .uid(orderNum%1000)
                .oid(orderNum)
                .produceContext(Collections.singletonList("test-" + orderNum))
                .rate(rate)
                .vertical(true)
                .scope(1000D)
                .totalNum(dn*rate*(r.nextInt(5) + 1))
                .build();
        Gson gson=new Gson();
        Message message=new Message("task_message_topic", "*", gson.toJson(taskMessage).getBytes());
        try {
            producer.send(message);
        } catch (MQClientException|RemotingException|InterruptedException|MQBrokerException e) {
            e.printStackTrace();
        }
    }

    private static void runOne(CommandLineArg arg, String equipName, AtomicLong count) {
        EventLoopGroup client=new NioEventLoopGroup(1);
        try {
            Bootstrap b=new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(client)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(arg.getServer(), arg.getPort())
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
                                if (!socketChannel.pipeline().channel().isActive()) {
                                    try {
                                        socketChannel.pipeline().channel().close().sync();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 3, TimeUnit.MINUTES);
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

    private static DefaultMQProducer createMq() {
        DefaultMQProducer producer=new DefaultMQProducer("test-order-message");
        try {
            // 设置NameServer的地址
            producer.setVipChannelEnabled(false);
            producer.setNamesrvAddr("47.111.185.61:9876");
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }

    private static CommandLineArg createCommand(String[] args) throws ParseException {
        final DefaultParser parser=new DefaultParser();
        final CommandLine commandline=parser.parse(OPTIONS, args);
        return CommandLineArg.builder()
                .endMsg(Integer.parseInt(commandline.getOptionValue("emsg")))
                .startMsg(Integer.parseInt(commandline.getOptionValue("smsg")))
                .isSend(Boolean.parseBoolean(commandline.getOptionValue("ismsg")))
                .startRunner(Integer.parseInt(commandline.getOptionValue("srun")))
                .endRunner(Integer.parseInt(commandline.getOptionValue("erun")))
                .server((String) commandline.getParsedOptionValue("s"))
                .port(Integer.parseInt(commandline.getOptionValue("p")))
                .build();
    }

    @Builder
    @Getter
    private static class CommandLineArg {
        private Integer startMsg;
        private Integer endMsg;
        private Integer startRunner;
        private Integer endRunner;
        private String server;
        private Integer port;
        private Boolean isSend;
    }


}
