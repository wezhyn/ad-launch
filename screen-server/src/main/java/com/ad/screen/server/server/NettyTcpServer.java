package com.ad.screen.server.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @ClassName NettyTcpServer
 * @Description TODO
 * @Param
 * @Author ZLB
 * @Date 2020/2/22 1:33
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyTcpServer {
    @Value("${netty.server.address}")
    private String address;

    @Value("${netty.server.port}")
    private int port;

    @Autowired
    ScreenChannelInitializer screenChannelInitializer;


    EventLoopGroup bossGroup=new NioEventLoopGroup();
    EventLoopGroup workerGroup=new NioEventLoopGroup();

    /**
     * 启动Server
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(screenChannelInitializer)
                .option(ChannelOption.SO_BACKLOG, 1024) //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .childOption(ChannelOption.TCP_NODELAY, true)//立即写出
                .childOption(ChannelOption.SO_KEEPALIVE, true)//长连接
                .attr(AttributeKey.valueOf("TASK_MAP"), null);//接收的任务列表
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);//内存泄漏检测 开发推荐PARANOID 线上SIMPLE
        ChannelFuture channelFuture=serverBootstrap.bind(address, port).sync();
        if (channelFuture.isSuccess()) {
            log.info("TCP服务启动完毕,port={}", this.port);
        }
    }

    /**
     * 销毁资源
     */
    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
        log.info("关闭成功");
    }

}
