package com.luo.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Created by xiangnan on 2018/9/17.
 */
public class PoolMemoryMain {

    public static void main(String[] args) {
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

        ByteBuf buf = allocator.heapBuffer(252);
        System.out.println(buf);

        buf.release();
    }

}
