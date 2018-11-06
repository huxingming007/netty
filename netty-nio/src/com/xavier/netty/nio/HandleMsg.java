package com.xavier.netty.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author huxingming
 * @date 2018/11/5-下午2:54
 * @Description TODO
 */
public class HandleMsg implements Runnable {

    SelectionKey sk;
    ByteBuffer bb;
    Selector selector;

    public HandleMsg(SelectionKey sk, ByteBuffer bb, Selector selector) {
        this.sk = sk;
        this.bb = bb;
        this.selector = selector;
    }


    @Override
    public void run() {

        EchoClient echoClient = (EchoClient) sk.attachment();
        // 简单业务逻辑处理：压入队列
        echoClient.enqueue(bb);
        // 重新注册感兴趣的消息事件，把op_write也作为感兴趣的事件
        sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        selector.wakeup();
    }
}
