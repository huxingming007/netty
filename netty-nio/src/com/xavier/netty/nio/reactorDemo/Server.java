package com.xavier.netty.nio.reactorDemo;


/**
 * @author huxingming
 * @date 2019/2/1-11:33 AM
 * @Description TODO
 */
public class Server {


    static Selector selector = new Selector();
    static Dispatcher eventLooper = new Dispatcher(selector);
    static Acceptor acceptor;

    Server(int port) {
        acceptor = new Acceptor(selector, port);
    }

    public static void start() {
        eventLooper.registEventHandler(EventType.ACCEPT, new AcceptEventHandler(selector));
        new Thread(acceptor, "Acceptor-" + acceptor.getPort()).start();
        eventLooper.handleEvents();
    }

    public static void main(String[] args) {

        Server server = new Server(9999);
        InputSource inputSource = new InputSource("要处理的数据",1);
        acceptor.addNewConnection(inputSource);
        start();
    }
}
