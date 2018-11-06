package com.xavier.netty.nio;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @author huxingming
 * @date 2018/11/5-下午2:23
 * @Description TODO
 */
public class EchoClient {

    private LinkedList<ByteBuffer> outq;

    EchoClient() {
        outq = new LinkedList<>();
    }

    public LinkedList<ByteBuffer> getOutq() {
        return outq;
    }

    public void enqueue(ByteBuffer bb) {
        outq.addFirst(bb);

    }
}
