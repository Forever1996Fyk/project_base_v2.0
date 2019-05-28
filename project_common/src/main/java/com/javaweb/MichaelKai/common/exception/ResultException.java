package com.javaweb.MichaelKai.common.exception;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import lombok.Data;

/**
 * @program: project_base
 * @description: 结果异常处理
 * @author: YuKai Fan
 * @create: 2019-05-27 11:56
 **/
@Data
public class ResultException extends RuntimeException {
    private Integer code;

    /**
     * 继承exception,加入错误状态值
     * @param resultEnum
     */
    public ResultException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getValue();
    }

    /**
     * 自定义错误信息
     * @param message
     * @param code
     */
    public ResultException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}