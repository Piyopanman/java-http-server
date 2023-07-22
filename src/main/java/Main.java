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
            // リクエストを標準出力
//            String line = reader.readLine();
//            while(!line.isEmpty()) {
//                System.out.println(line);
//                line = reader.readLine();
//            }

            // リクエストラインの解析
            // 空白で区切り、最初がメソッド、次がURI、最後がプロトコルバージョン
            String requestLine = reader.readLine();
            String[] requestLineElements = requestLine.split(" ");
            String httpMethod = requestLineElements[0];
            String path = requestLineElements[1];
            String protocolVersion = requestLineElements[2];
            // リクエストラインを出力
            System.out.printf("%s %s %s%s", httpMethod, path, protocolVersion, CRLF);
            // リクエストヘッダーを出力
            String line = reader.readLine();
            while(!line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }
            //


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
