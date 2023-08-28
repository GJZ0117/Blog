package com.gjz.handler;

import com.gjz.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class AllExceptionHandler {
    //异常处理，处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json数据
    public Result doException(Exception ex) {
        log.error(ex.getMessage());
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            log.error(stackTraceElement.toString());
        }
        return Result.fail(-999, "系统异常");
    }
}
