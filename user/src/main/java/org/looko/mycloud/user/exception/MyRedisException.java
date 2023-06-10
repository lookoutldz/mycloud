package org.looko.mycloud.user.exception;

public class MyRedisException extends RuntimeException {
    public MyRedisException() {
    }

    public MyRedisException(String message) {
        super(message);
    }

    public MyRedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyRedisException(Throwable cause) {
        super(cause);
    }

    public MyRedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
