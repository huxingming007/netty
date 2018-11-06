package com.xavier.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @author huxingming
 * @date 2018/11/2-下午2:47
 * @Description 模拟io等待场景
 */
public class HeavyClient {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static final int SLEEP_TIME = 1000*1000*1000;

    static class Client implements Runnable {

        @Override
        public void run() {

            Socket client = null;
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = null;
            try {
                client = new Socket();
                client.connect(new InetSocketAddress("127.0.0.1", 8000));
                printWriter = new PrintWriter(client.getOutputStream(), true);
                printWriter.print("h");
                LockSupport.parkNanos(SLEEP_TIME);
                printWriter.print("e");
                LockSupport.parkNanos(SLEEP_TIME);
                printWriter.print("l");
                LockSupport.parkNanos(SLEEP_TIME);
                printWriter.print("l");
                LockSupport.parkNanos(SLEEP_TIME);
                printWriter.print("o");
                printWriter.println();
                printWriter.flush();
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("from server:" + bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null)
                        client.close();
                    if (printWriter != null)
                        printWriter.close();
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public static void main(String[] args) {

        Client client = new Client();
        for (int i = 0; i < 10; i++) {
            executorService.execute(client);
        }


    }
}
