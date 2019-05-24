package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.Dict;
import com.javaweb.MichaelKai.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 数据字典
 * @author: YuKai Fan
 * @create: 2019-05-24 16:13:29
 *
 */
@RestController
@RequestMapping("/api")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 添加
     * @param dict
     * @return
     */
    @PostMapping("/dict")
    public Result addDict(@RequestBody Dict dict) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), dictService.addDict(dict));
    }

    /**
     * 编辑修改
     * @param dict
     * @return
     */
    @PutMapping("/dict")
    public Result editDictById(@RequestBody Dict dict) {
        dictService.editDictById(dict);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), dict);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/dict")
    public Result editDictById(@RequestParam String id) {
        dictService.delDictById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/dicts/{ids}")
    public Result editDictByIds(@PathVariable("ids") String[] ids) {
        dictService.delDictByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getDicts/noPage")
    public Result getDicts(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = dictService.getDicts(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getDicts")
    public Result getUsers(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = dictService.getDicts(start, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


}
