package org.looko.mycloud.user.exception;

public class MyEmailException extends RuntimeException {
    public MyEmailException() {
        super();
    }

    public MyEmailException(String message) {
        super(message);
    }

    public MyEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyEmailException(Throwable cause) {
        super(cause);
    }

    protected MyEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
