package com.xavier.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author huxingming
 * @date 2018/11/2-下午2:01
 * @Description 服务端
 */
public class Server {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    static class HandleMsg implements Runnable {
        private Socket clinetSocket;

        public HandleMsg(Socket clinetSocket) {
            this.clinetSocket = clinetSocket;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            PrintWriter printWriter = null;

            try {
                bufferedReader = new BufferedReader(new InputStreamReader(clinetSocket.getInputStream()));
                printWriter = new PrintWriter(clinetSocket.getOutputStream(),true);
                String inputLine = null;
                long b = System.currentTimeMillis();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    // 打印收到的消息
                    System.out.println("received：" + inputLine);
                    // 写回去
                    printWriter.println(inputLine);
                }
                long e = System.currentTimeMillis();
                System.out.println("time：" + (e - b) + "ms");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                    if (printWriter != null)
                        printWriter.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getRemoteSocketAddress() + "  connect!");
                executorService.execute(new HandleMsg(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
