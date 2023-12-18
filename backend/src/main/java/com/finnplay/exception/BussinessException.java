package com.finnplay.exception;

import lombok.Data;

@Data
public class BussinessException extends RuntimeException {
    private int code;
    private String msg;

    public BussinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BussinessException(String msg) {
        super(msg);
        this.code = ResponseCode.FAIL.getCode();
        this.msg = msg;
    }
}
