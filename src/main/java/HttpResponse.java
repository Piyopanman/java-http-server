package main.java;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static main.java.Constant.*;


public class HttpResponse {
    private final HttpRequest request;
    private final StatusLine statusLine;
    private final OutputStream out;
    private Map<String, String> headerFieldMap = new HashMap<>();
    private String body;
    

    public HttpResponse(OutputStream out, HttpRequest request, Status status) {
        this.request = request;
        this.out = out;
        statusLine = new StatusLine(status);
        headerFieldMap.put("Content-Type", ContentType.getMIMEType(request.getRequestLine().getPath()));
    }

    public void response()  {
        try {
            writeStatusLine();
            writeHeaderFields();
            this.out.write(CRLF.getBytes());
        } catch (IOException e) {
            System.err.println("Error in response method");
            e.printStackTrace();
        }
        writeBody();
    }

    private void writeStatusLine() throws IOException {
       this.out.write(String.format("%s %s%s", this.statusLine.getProtocolVersion(), this.statusLine.getStatus(), CRLF).getBytes());
    }

    private void writeHeaderFields() {
        headerFieldMap.forEach((key, value) -> {
            try {
                this.out.write(String.format("%s: %s%s", key, value, CRLF).getBytes());
            } catch (IOException e) {
                System.err.println("Error in writeHeaderFilelds method");
                throw new RuntimeException(e);
            }
        });
    }

    private void writeBody() {
        HttpRequest.RequestLine requestLine = this.request.getRequestLine();
        Path path = requestLine.getPath();
        // GETかつtext/html
        if(requestLine.getHttpMethod().equals(HttpMethod.GET.name())
                && Objects.equals(ContentType.getMIMEType(path), "text/html")) {
            try {
                if(this.statusLine.getStatus() == Status.OK) {
                    this.out.write(Files.readString(path).getBytes());
                } else if (this.statusLine.getStatus() == Status.NOT_FOUND) {
                    this.out.write(Files.readString(NOT_FOUND_PAGE_PATH).getBytes());
                }
            } catch (IOException e) {
                System.err.println("Error in writeBody method");
            }

        }if(requestLine.getHttpMethod().equals(HttpMethod.POST.name())) {
            /*
            TODO: メッセージボディで渡ってきたものを、 Content-Length分読み、標準出力する
            必要なもの：リクエストのメッセージボディ、Content-Length
            メッセージボディの読み方：リクエスト側でもう読めている？注意：ヘッダーフィールドはマップで保持している
            出力の仕方：標準出力
             */
        }
    }

    private boolean isContentTypeHtml() {
        Path path = this.request.getRequestLine().getPath();
        return path.toString().endsWith("html");
    }



    class StatusLine {
        private final String protocolVersion = "HTTP/1.1";
        private final Status status;

        public StatusLine(Status status) {
            this.status = status;
        }

        // Getter
        public String getProtocolVersion() {
            return this.protocolVersion;
        }

        public Status getStatus(){
            return this.status;
        }

    }
}
