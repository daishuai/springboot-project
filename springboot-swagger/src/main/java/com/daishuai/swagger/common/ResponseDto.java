package com.daishuai.swagger.common;

import lombok.Data;

/**
 * @author Daishuai
 * @date 2020/4/19 16:00
 */
@Data
public class ResponseDto<T> {

    private int status;

    private String code;

    private String message;

    private T result;

    public ResponseDto(int status, String code, String message, T result) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static ResponseDto success() {
        return ResponseDto.success(null);
    }

    public static <T> ResponseDto<T> success(T t) {
        return new ResponseDto<T>(200, "0", "", t);
    }

    public static ResponseDto error(String message) {
        return new ResponseDto(500, "-1", message, null);
    }
}
