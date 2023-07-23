package main.java;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static main.java.Constant.*;


public class HttpResponse {
    private final HttpRequest request;
    private final StatusLine statusLine;
    private final OutputStream out;
    private Map<String, String> headerField = new HashMap<>();
    private String body;

    public HttpResponse(OutputStream out, HttpRequest request, Status status) {
        this.request = request;
        this.out = out;
        statusLine = new StatusLine(status);
    }

    public void response() throws IOException {
        writeStatusLine();
        writeHeaderFields();
        this.out.write(CRLF.getBytes());
        writeBody();
    }

    private void writeStatusLine() throws IOException {
       this.out.write(String.format("%s %s%s", this.statusLine.getProtocolVersion(), this.statusLine.getStatus(), CRLF).getBytes());
    }

    private void writeHeaderFields() throws IOException {
        // TODO: Content-Typeをどうにかいい感じにする(mapにいれたヘッダーフィールドを返す)
        this.out.write("Content-type: text/html; charset=UTF-8\r\n".getBytes());
    }

    private void writeBody() throws IOException {
        HttpRequest.RequestLine requestLine = this.request.getRequestLine();
        // GETかつtext/html
        if(Objects.equals(requestLine.getHttpMethod(), HttpMethod.GET.name()) && isContentTypeHtml()) {
            if(this.statusLine.getStatus() == Status.OK) {
                this.out.write(Files.readString(requestLine.getPath()).getBytes());
            } else if (this.statusLine.getStatus() == Status.NOT_FOUND) {
                this.out.write(Files.readString(NOT_FOUND_PAGE_PATH).getBytes());
            }
        }
    }

    private boolean isContentTypeHtml() {
        Path path = this.request.getRequestLine().getPath();
        return path.toString().endsWith("html");
    }



    // memo
    // - make enum of content-type
    
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
