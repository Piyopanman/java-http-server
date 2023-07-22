package main.java;

import jdk.jfr.ContentType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static main.java.Constant.*;


public class HttpResponse {
    private HttpRequest request;
    private StatusLine statusLine;
    private OutputStream out;
    private Map<String, String> headerField = new HashMap<>();
    private String body;

    public HttpResponse(OutputStream out, HttpRequest request) {
        this.request = request;
        this.out = out;
    }

    public void response() throws IOException {
        writeStatusLine();
        writeHeaderFields();
        this.out.write(CRLF.getBytes());
        writeBody();
    }

    private void writeStatusLine() throws IOException {
        // TODO: ステータスコードとかをいい感じに取得する（メンバクラスを利用）
       this.out.write("HTTP/1.1 200 OK\r\n".getBytes());
    }

    private void writeHeaderFields() throws IOException {
        // TODO: Content-Typeをどうにかいい感じにする(mapにいれたヘッダーフィールドを返す)
        this.out.write("Content-type: text/html; charset=UTF-8\r\n".getBytes());
    }

    private void writeBody() throws IOException {
        HttpRequest.RequestLine requestLine = this.request.getRequestLine();
        // GETかつtext/html
        if(Objects.equals(requestLine.getHttpMethod(), HttpMethod.GET.name()) && isContentTypeHtml()) {
            Path path = requestLine.getPath();
            if(Files.exists(path)) {
                this.out.write(Files.readString(path).getBytes());
            } else {
                this.out.write(Files.readString(NOT_FOUND_PAGE_PATH).getBytes());
            }
        }
    }


    private boolean isContentTypeHtml() {
        // pathの拡張子で判断する
        Path path = this.request.getRequestLine().getPath();
        return path.toString().endsWith("html");
    }



    // memo
    // - make enum of content-type

    // メンバクラスとしてStatusLineクラスを定義
    class StatusLine {
        private String protocolVersion;
        private Map<Integer, String> statusMap;

        public StatusLine() {

        }
    }
}
