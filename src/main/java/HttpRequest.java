package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.java.Constant.CRLF;

public class HttpRequest {
    private RequestLine requestLine;
    private Map<String, String> headerField = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        this.requestLine = new RequestLine(reader.readLine());
        putRequestHeaderField(reader);
    }

    private void putRequestHeaderField(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        while(!line.isEmpty()) {
            List<String> field = Arrays.stream(line.split(":")).map(String::trim).toList();
            headerField.put(field.get(0), field.get(1));
            line = reader.readLine();
        }
    }

    public void stdOutputMessage() {
        stdOutputRequestLine();
        stdOutputRequestHeaderField();
    }

    private void stdOutputRequestLine() {
        System.out.println(this.requestLine);
    }

    private void stdOutputRequestHeaderField() {
        headerField.entrySet()
                .forEach(field -> System.out.printf("%s: %s%s", field.getKey(), field.getValue(), CRLF));
    }

    // Getter
    public RequestLine getRequestLine() {
        return this.requestLine;
    }


    // メンバクラスとしてRequestLineクラスを定義
    class RequestLine {
        private String requestLine;
        private String httpMethod;
        private Path path;  // Pathクラスみたいなやつあった気がするが一旦ストリング
        private String protocolVersion;

        public RequestLine(String requestLine) {
            this.requestLine = requestLine;
            String[] requestLineElements = requestLine.split(" ");
            this.httpMethod = requestLineElements[0];
            String decodedPath = URLDecoder.decode(requestLineElements[1].substring(1), StandardCharsets.UTF_8);
            this.path = Paths.get(decodedPath);
            this.protocolVersion = requestLineElements[2];
        }

        // Getter
        public String getHttpMethod() {
            return this.httpMethod;
        }
        public Path getPath() {
            return this.path;
        }


        @Override
        public String toString() {
            return this.requestLine;
        }
    }

}
