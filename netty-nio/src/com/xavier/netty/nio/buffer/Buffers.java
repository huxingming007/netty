package com.xavier.netty.nio.buffer;

import java.nio.ByteBuffer;

/**
 * @author huxingming
 * @date 2018/11/5-上午11:19
 * @Description 自定义Buffer类中包含读缓冲区和写缓冲区，用于注册通道时的附加对象
 */
public class Buffers {

    ByteBuffer readBuffer;
    ByteBuffer writeBuffer;

    public Buffers(int readCapacity, int writeCapacity) {
        readBuffer = ByteBuffer.allocate(readCapacity);
        writeBuffer = ByteBuffer.allocate(writeCapacity);
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer gerWriteBuffer() {
        return writeBuffer;
    }

}
