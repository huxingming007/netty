package com.xavier.netty.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @author huxingming
 * @date 2018/11/5-上午9:49
 * @Description TODO
 */
public class BufferDemo1 {

    public static void main(String[] args) {

        System.out.println("before allocate:" + Runtime.getRuntime().freeMemory());
        // 堆中申请10M的内存
        ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
        System.out.println(" after allocate:" + Runtime.getRuntime().freeMemory());

        // 直接内存（理解为计算机的物理内存）申请10M的内存
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);
        // 跟上面的那个值是一样的  因为allocatedirect 不是从堆中申请内存
        System.out.println(" after allocate:" + Runtime.getRuntime().freeMemory());


    }
}
