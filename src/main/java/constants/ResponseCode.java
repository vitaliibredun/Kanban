package constants;

public enum ResponseCode {
    OK(200),
    FORBIDDEN(403),
    BAD_REQUEST(400),
    METHOD_NOT_ALLOWED(405),
    NOT_FOUND(404),
    CREATED(201),

    ;

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }
}
