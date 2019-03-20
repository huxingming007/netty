package com.xavier.netty.nio.reactorDemo;

/**
 * @author huxingming
 * @date 2019/2/1-11:27 AM
 * @Description reactor模式中内部处理的event类
 */
public class Event {

    private InputSource source;
    private EventType type;

    public InputSource getSource() {
        return source;
    }

    public void setSource(InputSource source) {
        this.source = source;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
