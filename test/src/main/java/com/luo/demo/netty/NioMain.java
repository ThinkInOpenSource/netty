package com.luo.demo.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author xiangnan
 * date 2018/8/5 21:59
 */
public class NioMain {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(8080));
        // non-block io
        serverChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(10000) == 0) {
                System.out.println("selector.select loop...");
                continue;
            }

            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                // accept event
                if (key.isAcceptable()) {
                    handlerAccept(selector, key);
                }

                // socket event
                if (key.isReadable()) {
                    handlerRead(key);
                }

                /**
                 * Selector不会自己从已选择键集中移除SelectionKey实例，必须在处理完通道时手动移除。
                 * 下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
                 */
                iter.remove();
            }
        }
    }

    static void handlerAccept(Selector selector, SelectionKey key) throws IOException {
        System.out.println("coming a new client...");
        SocketChannel channel = ((ServerSocketChannel) key.channel()).accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
    }

    static void handlerRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();

        int num = channel.read(buffer);
        if (num <= 0) {
            // error or fin
            System.out.println("close " + channel.getRemoteAddress());
            channel.close();
        } else {
            buffer.flip();
            String recv = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
            System.out.println("recv: " + recv);

            buffer = ByteBuffer.wrap(("server: " + recv).getBytes());
            channel.write(buffer);
        }
    }
}
