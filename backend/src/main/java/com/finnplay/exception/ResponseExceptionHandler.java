package com.finnplay.exception;

import com.finnplay.vo.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
@ResponseBody
public class ResponseExceptionHandler {

    @ExceptionHandler(BussinessException.class)
    public ResponseResult<String> businessException(BussinessException e) {
        log.error("BusinessException code={},  msg= {}", e.getCode(), e.getMessage(), e);
        return ResponseResult.fail(e.getCode(), e.getMsg());
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult parameterMissingHandler(MethodArgumentNotValidException e) {
        var defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("MethodArgumentNotValidException msg= {}", defaultMessage, e);
        return ResponseResult.fail(defaultMessage);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseResult<String> nullPointerException(NullPointerException e) {
        log.error("NullPointerException = {}", e.getMessage(), e);
        return ResponseResult.fail(ResponseCode.RC400.getCode(), ResponseCode.RC400.getMsg());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseResult<String> noHandlerFoundException(HttpServletRequest req, Exception e) {
        log.error("NoHandlerFoundException, method = {}, path = {} ", req.getMethod(), req.getServletPath(), e);
        return ResponseResult.fail(ResponseCode.RC404.getCode(), ResponseCode.RC404.getMsg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseResult<String> HttpRequestMethodNotSupportedException(HttpServletRequest req, Exception e) {
        log.error("HttpRequestMethodNotSupportedException, method = {}, path = {} ", req.getMethod(), req.getServletPath(), e);
        return ResponseResult.fail(ResponseCode.RC405.getCode(), ResponseCode.RC405.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<String> exception(Exception e) {
        log.error("Exception = {}", e.getMessage(), e);
        return ResponseResult.fail(ResponseCode.RC500.getCode(), ResponseCode.RC500.getMsg());
    }


}