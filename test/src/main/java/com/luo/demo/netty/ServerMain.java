package com.luo.demo.netty;

import com.luo.demo.netty.handler.EchoHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * netty server
 *
 * @author xiangnan
 * date 2018/8/4 10:04
 */
public class ServerMain {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(4);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // NioChannel方式
                    .localAddress(60000)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoHandler());
                        }
                    });

            // start
            ChannelFuture future = boot.bind().sync();
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // shutdown
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
