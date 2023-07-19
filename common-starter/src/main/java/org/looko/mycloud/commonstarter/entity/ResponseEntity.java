package org.looko.mycloud.commonstarter.entity;


import org.looko.mycloud.commonstarter.enumeration.ResponseStatus;
import org.looko.mycloud.commonstarter.util.JsonUtil;

import java.io.Serializable;

import static org.looko.mycloud.commonstarter.enumeration.BasicResponseStatus.SERVER_ERROR;
import static org.looko.mycloud.commonstarter.enumeration.BasicResponseStatus.SUCCESS;

public class ResponseEntity<T> implements Serializable {

    private final ResponseStatus status;

    private final T result;

    private ResponseEntity(ResponseStatus status, T result) {
        this.status = status;
        this.result = result;
    }

    public int getStatusCode() {
        return status.getCode();
    }
    public String getStatusMessage() {
        return status.getMessage();
    }
    public T getResult() {
        return result;
    }

    public static <T> ResponseEntity<T> success(T result) {
        return new ResponseEntity<>(SUCCESS, result);
    }

    public static <T> ResponseEntity<T> failure() {
        return new ResponseEntity<>(SERVER_ERROR, null);
    }

    public static <T> ResponseEntity<T> failure(T result) {
        return new ResponseEntity<>(SERVER_ERROR, result);
    }

    public static <T> ResponseEntity<T> failure(ResponseStatus status) {
        return new ResponseEntity<>(status, null);
    }

    public static <T> ResponseEntity<T> failure(ResponseStatus status, T result) {
        return new ResponseEntity<>(status, result);
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }
}
