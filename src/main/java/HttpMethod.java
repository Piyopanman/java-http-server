package main.java;

public enum HttpMethod {
    POST("POST"),
    GET("GET"),
    ;

    private final String methodName;
    HttpMethod(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return this.methodName;
    }


}
