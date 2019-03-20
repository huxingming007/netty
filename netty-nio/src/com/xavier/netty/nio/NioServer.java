package com.xavier.netty.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author huxingming
 * @date 2018/11/5-下午1:48
 * @Description TODO
 */
public class NioServer {

    private static Selector selector;

    // 服务端对读取到的消息进行异步处理
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    // 记录服务端线程在一个客户端所花的时间
    private static Map<Socket, Long> time_stat = new HashMap<>();

    public static void main(String[] args) throws IOException {

        startServer();
    }

    private static void startServer() throws IOException {
        // 创建选择器
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);// 设置为非阻塞

        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.socket().bind(isa);
        // 把ServerSocketChannel注册到selector上，感兴趣的事件是accept，当selector发现ServerSocketChannel有新的客户端连接，就会通知ServerSocketChannel进行处理
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        long e = 0L;
        for (; ; ) {
            // 没有任何数据准备好 会一直阻塞 返回值是已经准备就绪的SelectionKey的数量
            int select = selector.select();
            // 获得准备好的SelectionKey，selector为多个channel服务，准备就绪的channel可能也有多个
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey sk = i.next();
                // 不移除的话 会重复处理SelectionKey
                i.remove();
                if (sk.isAcceptable()) {
                    doAccept(sk);
                } else if (sk.isValid() && sk.isReadable()) {
                    if (!time_stat.containsKey(((SocketChannel) sk.channel()).socket()))
                        time_stat.put(((SocketChannel) sk.channel()).socket(), System.currentTimeMillis());
                    doRead(sk);
                } else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    e = System.currentTimeMillis();
                    long b = time_stat.remove(((SocketChannel) sk.channel()).socket());
                    System.out.println("time:" + (e - b) + "ms");
                }
            }
        }
    }

    private static void doWrite(SelectionKey sk) {
        // SelectionKey与doRead方法中的 是同一个  通过这个可以共享EchoClient
        SocketChannel channel = (SocketChannel) sk.channel();
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutq();
        ByteBuffer bb = outq.getLast();
        try {
            // 写回去
            int len = channel.write(bb);
            if (len == -1) {
                // todo
                //disconnect(sk);
                return;
            }
            if (bb.remaining() == 0) {
                outq.removeLast();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (outq.size() == 0) {
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private static void doRead(SelectionKey sk) {

        // 这个SelectionKey与doAccept中新生成的SelectionKey是同一个
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;
        try {
            // 把读取到的数据放入bb中
            len = channel.read(bb);
            if (len < 0) {
                // todo
                // disconnect(sk);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 反转  准备让bb被读取  position=0 limit=前position位置
        bb.flip();
        Charset utf8 = Charset.forName("UTF-8");
        // decode会是position=limit
        CharBuffer cb = utf8.decode(bb);
        System.out.println(cb.array());
        // 使position=0
        bb.rewind();
        // 异步处理读取到的数据
        executorService.execute(new HandleMsg(sk, bb, selector));
    }

    private static void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;
        try {
            // 代表和客户端通信的通道
            clientChannel = server.accept();
            clientChannel.configureBlocking(false);
            // 新生成的SocketChannel注册到selector，并且感兴趣的事件是read，当selector发现这个channel已经准备好读时，就能给线程一个通知
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            // 作为附件到连接的SelectionKey
            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);
            InetAddress inetAddress = clientChannel.socket().getInetAddress();
            System.out.println("accept connection from :" + inetAddress.getHostAddress() + ".");
        } catch (IOException e) {
            System.out.println("fail to accept new client.");
            e.printStackTrace();
        }
    }
}
