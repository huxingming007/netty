package com.xavier.netty.nio.reactorDemo;

/**
 * @author huxingming
 * @date 2019/2/1-11:28 AM
 * @Description event处理器的抽象类
 */
public abstract class EventHandler {

    private InputSource source;
    public abstract void handle(Event event);

    public InputSource getSource() {
        return source;
    }

    public void setSource(InputSource source) {
        this.source = source;
    }
}
