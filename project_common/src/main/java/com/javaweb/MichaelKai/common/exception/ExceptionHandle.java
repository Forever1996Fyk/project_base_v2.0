package com.javaweb.MichaelKai.common.exception;

import com.javaweb.MichaelKai.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: project_base
 * @description: 异常通知类
 * @author: YuKai Fan
 * @create: 2019-05-27 11:59
 **/
@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    /**
     * 拦截自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e) {
        if (e instanceof ResultException) {
            ResultException resultException = (ResultException) e;
            return new Result(false, resultException.getCode(), resultException.getMessage());
        }

        e.printStackTrace();
        log.error("【系统异常】{}", e);
        return new Result(false, 500, e.getMessage());
    }
}