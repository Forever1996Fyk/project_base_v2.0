package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.service.BasicDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 数据字典controller
 * @author: YuKai Fan
 * @create: 2019-05-24 16:14
 **/
@RestController
@RequestMapping("/api/basicDict")
public class BasicDictController {

    @Autowired
    private BasicDictService basicDictService;

    /**
     * 获取数据字典项
     * @param map
     * @return
     */
    @GetMapping("/getDictItem")
    public Result getDictItem(Map<String, Object> map) {
        List<Map<String, Object>> list = basicDictService.getDictItem(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 添加数据字典
     * @param map
     * @return
     */
    @PostMapping("/addDict")
    public Result addDict(Map<String, Object> map) {
        basicDictService.addDict(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }
}