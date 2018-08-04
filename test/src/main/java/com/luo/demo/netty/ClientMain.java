package com.luo.demo.netty;

import com.luo.demo.netty.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty client
 *
 * @author xiangnan
 * date 2018/8/4 13:04
 */
public class ClientMain {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    //p.addLast(new LoggingHandler(LogLevel.INFO));
                    p.addLast(new EchoClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("localhost", 8081).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
