package com.android.common.exception;


import com.android.common.ErrorCode;
import com.android.common.BaseResponse;
import com.android.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

//    @ExceptionHandler(Exception.class)
//    public BaseResponse<?> exceptionHandler(Exception e) {
//        log.error("exception", e);
//        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
//    }

}
