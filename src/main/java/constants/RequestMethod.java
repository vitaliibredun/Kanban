package constants;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),

    ;

    private final String method;

    RequestMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
