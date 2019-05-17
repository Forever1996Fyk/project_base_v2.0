package com.javaweb.MichaelKai.common.vo;

/**
 * @program: project_base
 * @description: 返回结果vo
 * @author: YuKai Fan
 * @create: 2019-05-17 15:04
 **/
public class Result {

    private boolean flag;//返回结果是否成功标识
    private Integer code;//返回结果码
    private String message;//返回结果信息
    private Object data;//返回数据

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}