package com.xavier.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author huxingming
 * @date 2018/11/2-下午2:23
 * @Description 客户端
 */
public class BioClient {

    public static void main(String[] args) {

        Socket client = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;

        try {
            client = new Socket();
            client.connect(new InetSocketAddress("127.0.0.1", 8000));
            printWriter = new PrintWriter(client.getOutputStream(), true);
            printWriter.println("hello netty bio");
            printWriter.flush();
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("from server:" + bufferedReader.readLine());
        } catch (Exception e) {
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
