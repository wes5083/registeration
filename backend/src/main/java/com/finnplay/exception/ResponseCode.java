package com.finnplay.exception;

public enum ResponseCode {
    SUCCESS(1,"success"),
    FAIL(-1, "fail"),
    RC400(400, "400 exception"),
    RC404(404, "404 exception"),
    RC405(405, "405 exception"),
    RC500(500, "500 exception");

    private final int code;
    private final String msg;

    ResponseCode(int code, String msg) {
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