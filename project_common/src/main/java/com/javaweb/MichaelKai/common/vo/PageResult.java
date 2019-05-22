package com.javaweb.MichaelKai.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: project_base
 * @description: 分页实体
 * @author: YuKai Fan
 * @create: 2019-05-22 14:23
 **/
@Data
public class PageResult<T> {
    private long total;

    private List<T> rows;

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}