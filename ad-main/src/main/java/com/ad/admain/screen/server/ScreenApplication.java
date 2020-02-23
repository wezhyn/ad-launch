package com.ad.admain.screen.server;

import com.ad.admain.screen.handler.HeartBeatMsgMsgHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author wezhyn
 * @since 02.19.2020
 */
public class ScreenApplication {


    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            ServerBootstrap b=new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            final ByteBuf delimiter=Unpooled.copiedBuffer("SOF".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(55, false, delimiter));
                            ch.pipeline().addLast(new HeartBeatMsgMsgHandler());
                            ch.pipeline().addLast(new IdleStateHandler(0,0,60));
                        }
                    });

            ChannelFuture f=b.bind(8888).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
