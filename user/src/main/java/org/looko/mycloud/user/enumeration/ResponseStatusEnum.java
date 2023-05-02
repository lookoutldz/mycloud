package org.looko.mycloud.user.enumeration;

public enum ResponseStatusEnum {
    OK(200, "ok"),
    AUTH_FAILED(401, "auth failed"),
    FORBIDDEN(403, "forbidden"),
    SERVER_ERROR(500, "server error"),
    BUSINESS_ERROR(600, "business error"),
    UNKNOWN_ERROR(99999, "unknown error");

    private final int code;
    private final String msg;

    ResponseStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
