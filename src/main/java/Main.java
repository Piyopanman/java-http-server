package main.java;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static main.java.Constant.CRLF;

public class Main {
    public static void main(String[] args) {
        System.out.println("start >>>");
        try (
                ServerSocket serverSocket = new ServerSocket(80);
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
        ) {

            HttpRequest request = new HttpRequest(in);
            request.stdOutputMessage();

            // レスポンス
            out.write("HTTP/1.1 200 OK\r\n".getBytes());
            out.write("Content-type: text/html; charset=UTF-8\r\n".getBytes());
            out.write(CRLF.getBytes());
            Path path = request.getRequestLine().getPath();
            out.write(Files.readString(path).getBytes());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println(">>> end");

    }
}
