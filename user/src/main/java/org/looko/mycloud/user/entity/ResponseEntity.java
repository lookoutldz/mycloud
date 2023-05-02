package org.looko.mycloud.user.entity;

import org.looko.mycloud.user.enumeration.ResponseStatusEnum;

import java.io.Serializable;
import java.util.Map;

public class ResponseEntity<T> implements Serializable {

    private static final String RESULT_KEY = "data";

    private final ResponseStatusEnum responseStatusEnum;

    private final Map<String, Object> resultMap;

    private ResponseEntity(ResponseStatusEnum responseStatusEnum, Map<String, Object> resultMap) {
        this.responseStatusEnum = responseStatusEnum;
        this.resultMap = resultMap;
    }

    public int getStatusCode() {
        return responseStatusEnum.getCode();
    }

    public String getStatusMessage() {
        return responseStatusEnum.getMsg();
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(ResponseStatusEnum.OK, Map.of(RESULT_KEY, data));
    }

    public static <T> ResponseEntity<T> failure() {
        return new ResponseEntity<>(ResponseStatusEnum.BUSINESS_ERROR, Map.of());
    }

    public static <T> ResponseEntity<T> failure(ResponseStatusEnum responseStatusEnum) {
        return new ResponseEntity<>(responseStatusEnum, Map.of());
    }

    public static <T> ResponseEntity<T> failure(ResponseStatusEnum responseStatusEnum, T data) {
        return new ResponseEntity<>(responseStatusEnum, Map.of(RESULT_KEY, data));
    }
}