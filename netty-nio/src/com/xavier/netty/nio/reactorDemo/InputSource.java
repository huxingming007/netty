package com.xavier.netty.nio.reactorDemo;

/**
 * @author huxingming
 * @date 2019/2/1-11:26 AM
 * @Description 输入对象，reactor模式中处理的原始输入对象
 */
public class InputSource {
    private Object data;
    private long id;

    public InputSource(Object data, long id) {
        this.data = data;
        this.id = id;
    }

    @Override
    public String toString() {
        return "InputSource{" +
                "data=" + data +
                ", id=" + id +
                '}';
    }
}
