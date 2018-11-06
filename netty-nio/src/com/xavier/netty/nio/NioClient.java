package com.xavier.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * @author huxingming
 * @date 2018/11/5-下午8:10
 * @Description TODO
 */
public class NioClient {

    private static Selector selector;

    public static void init(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        selector = SelectorProvider.provider().openSelector();
        channel.connect(new InetSocketAddress(ip, port));
        // 感兴趣的事件是连接事件
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    public static void work() throws IOException {

        while (true) {
            if (!selector.isOpen()) {
                break;
            }
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();
                iterator.remove();
                // 连接事件发生
                if (key.isConnectable()) {
                    connect(key);
                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }

    }

    private static void read(SelectionKey key) throws IOException {

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        channel.read(buffer);
        byte[] array = buffer.array();
        String msg = new String(array).trim();
        System.out.println("客户端收取到的消息：" + msg);
        channel.close();
        key.selector().close();
    }

    private static void connect(SelectionKey key) throws IOException {

        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        channel.write(ByteBuffer.wrap(new String("hello netty nio\r\n").getBytes()));
        // 注册读事件
        channel.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {

        init("127.0.0.1", 8000);
        work();

    }
}
