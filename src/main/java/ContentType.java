package main.java;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ContentType {
    TEXT_PLAIN("text/plain", "txt"),
    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    IMAGE_PNG("image/png", "png"),
    JPEG("image/jpeg", "jpeg"),
    ;

    private static final Map<String, ContentType> EXTENSION_TO_CONTENT_TYPE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(contentType -> contentType.extension, Function.identity()));
    private final String MIMEType;
    private final String extension;


    ContentType(String MIMEType, String extension) {
        this.MIMEType = MIMEType;
        this.extension = extension;
    }
    
    public static String getMIMEType(Path path) {
        String extension = path.toString().split("\\.")[1];
        ContentType contentType = EXTENSION_TO_CONTENT_TYPE_MAP.get(extension);
        return Objects.requireNonNullElse(contentType, TEXT_PLAIN).MIMEType;
    }

    @Override
    public String toString() {
        return this.MIMEType;
    }
    
}
