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

            HttpResponse response = new HttpResponse(out, request);
            response.response();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println(">>> end");

    }
}
