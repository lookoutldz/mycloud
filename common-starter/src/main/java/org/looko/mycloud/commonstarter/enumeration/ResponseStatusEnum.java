package org.looko.mycloud.commonstarter.enumeration;

public enum ResponseStatusEnum {
    // common
    SUCCESS(100000, "成功"),

    // user
    BUSINESS_ERROR(102000, "业务通用错误"),
    AUTH_FAILED(102001, "验证失败"),
    FORBIDDEN(102002, "没有权限"),

    // tbd
    UNKNOWN_ERROR(0, "未知错误");

    private final int code;
    private final String message;

    ResponseStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
