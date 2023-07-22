package main.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static main.java.Constant.CRLF;

public class Main {
    public static void main(String[] args) {
        System.out.println("start >>>");
        try (
                ServerSocket serverSocket = new ServerSocket(80);
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ){

            HttpRequest request = new HttpRequest(in);
            request.stdOutputMessage();


            // レスポンス
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-type: text/plain; charset=UTF-8\r\n".getBytes());
            out.write(CRLF.getBytes());
            out.write("Hello, World!\r\n".getBytes());
            out.flush();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println(">>> end");

    }
}
