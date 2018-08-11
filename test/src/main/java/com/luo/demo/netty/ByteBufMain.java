package com.luo.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @author xiangnan
 * date 2018/8/5 23:51
 */
public class ByteBufMain {

    public static void main(String[] args) {

        // 无参数的工厂方法，还可以，调用buffer(int initialCapacity, int maxCapacity) 来指定初始化和最大容量限制。
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer();

        byteBuf.writeByte(3);

        System.out.println("get value "+(byteBuf.readByte())+" from buffer");

        try {
            // 再读取一个字节，超过了读取限制，则抛出异常。
            System.out.println("get value " + (byteBuf.readByte()) + " from buffer");
        } catch (Exception e){
            e.printStackTrace();
        }

        // 清空索引值置0
        byteBuf.clear();

        //循环写入257个字节，超过了默认初始化容量256的限制，则会触发扩容操作。
        for (int i = 0; i < 257; i++) {
            byteBuf.writeByte(i);
        }

        System.out.println("done.");
    }
}
