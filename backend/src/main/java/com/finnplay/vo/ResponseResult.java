package com.finnplay.vo;

import com.finnplay.exception.ResponseCode;
import lombok.Data;

@Data
public class ResponseResult<T> {
    private Integer code;
    private String msg;
    private T data;
    private long timestamp;

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> r = new ResponseResult<>();
        r.setCode(ResponseCode.SUCCESS.getCode());
        r.setMsg(ResponseCode.SUCCESS.getMsg());
        r.setData(data);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    public static <T> ResponseResult<T> fail(int code, String msg) {
        ResponseResult<T> r = new ResponseResult<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(null);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    public static <T> ResponseResult<T> fail(String msg) {
        ResponseResult<T> r = new ResponseResult<>();
        r.setCode(ResponseCode.FAIL.getCode());
        r.setMsg(msg);
        r.setData(null);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }
}