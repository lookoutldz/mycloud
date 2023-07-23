package org.looko.mycloud.commonstarter.enumeration;

public enum BasicResponseStatus implements ResponseStatus {
    SUCCESS(200, "🥳成功"),
    SERVER_ERROR(500, "🤢出了点问题"),
    AUTH_FAILED(401, "🤯身份验证失败"),
    FORBIDDEN(403, "🫣权限不足"),
    TO_MANY_REQUEST(429, "🥵手速太快辣~请慢点"),
    UNKNOWN_ERROR(999, "👽未知错误");

    private final Integer code;
    private final String message;

    BasicResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
