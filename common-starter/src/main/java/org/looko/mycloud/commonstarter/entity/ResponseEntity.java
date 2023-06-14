package org.looko.mycloud.commonstarter.entity;


import org.looko.mycloud.commonstarter.enumeration.ResponseStatusEnum;

import java.io.Serializable;
import java.util.Map;

public class ResponseEntity<T> implements Serializable {

    private static final String RESULT_KEY = "data";

    private static final String EMPTY_STRING = "";

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
        return responseStatusEnum.getMessage();
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(ResponseStatusEnum.SUCCESS, Map.of(RESULT_KEY, data == null ? EMPTY_STRING : data));
    }

    public static <T> ResponseEntity<T> failure() {
        return new ResponseEntity<>(ResponseStatusEnum.BUSINESS_ERROR, Map.of());
    }

    public static <T> ResponseEntity<T> failure(ResponseStatusEnum responseStatusEnum) {
        return new ResponseEntity<>(responseStatusEnum, Map.of());
    }

    public static <T> ResponseEntity<T> failure(ResponseStatusEnum responseStatusEnum, T data) {
        return new ResponseEntity<>(responseStatusEnum, Map.of(RESULT_KEY, data == null ? EMPTY_STRING : data));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("ResponseEntity{statusCode=").append(responseStatusEnum.getCode())
                .append(",statusMessage=").append(responseStatusEnum.getMessage())
                .append(",resultMap={");
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue().toString()).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("}}");
        return  stringBuilder.toString();
    }
}
