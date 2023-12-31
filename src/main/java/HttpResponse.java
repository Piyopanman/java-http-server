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

    public void response() throws IOException {
        writeStatusLine();
        writeHeaderFields();
        this.out.write(CRLF.getBytes());
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
                throw new RuntimeException(e);
            }
        });
    }

    private void writeBody() throws IOException {
        HttpRequest.RequestLine requestLine = this.request.getRequestLine();
        Path path = requestLine.getPath();
        // GETかつtext/html
        if(Objects.equals(requestLine.getHttpMethod(), HttpMethod.GET.name()) 
                && Objects.equals(ContentType.getMIMEType(path), "text/html")) {
            if(this.statusLine.getStatus() == Status.OK) {
                this.out.write(Files.readString(path).getBytes());
            } else if (this.statusLine.getStatus() == Status.NOT_FOUND) {
                this.out.write(Files.readString(NOT_FOUND_PAGE_PATH).getBytes());
            }
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
