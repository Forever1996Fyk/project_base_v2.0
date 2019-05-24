package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.DictItem;
import com.javaweb.MichaelKai.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 数据字典项
 * @author: YuKai Fan
 * @create: 2019-05-24 16:13:29
 *
 */
@RestController
@RequestMapping("/api")
public class DictItemController {
    @Autowired
    private DictItemService dictItemService;

    /**
     * 添加
     * @param dictItem
     * @return
     */
    @PostMapping("/dictItem")
    public Result addDictItem(@RequestBody DictItem dictItem) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), dictItemService.addDictItem(dictItem));
    }

    /**
     * 编辑修改
     * @param dictItem
     * @return
     */
    @PutMapping("/dictItem")
    public Result editDictItemById(@RequestBody DictItem dictItem) {
        dictItemService.editDictItemById(dictItem);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), dictItem);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/dictItem")
    public Result editDictItemById(@RequestParam String id) {
        dictItemService.delDictItemById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/dictItems/{ids}")
    public Result editDictItemByIds(@PathVariable("ids") String[] ids) {
        dictItemService.delDictItemByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getDictItems/noPage")
    public Result getDictItems(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = dictItemService.getDictItems(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getDictItems")
    public Result getUsers(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = dictItemService.getDictItems(start, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


}
