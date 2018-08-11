package com.luo.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * @author xiangnan
 * date 2018/8/11
 */
public class MyIdleStateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;

        System.out.println("[" + Thread.currentThread().getName() + "] : recv: " + in.toString(CharsetUtil.UTF_8));
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * idle回调方法
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            System.out.println(event.isFirst() + " " + event.state());

            ByteBuf result = ctx.channel().alloc().buffer();
            result.writeBytes("reader idle".getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(result);
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }
}
