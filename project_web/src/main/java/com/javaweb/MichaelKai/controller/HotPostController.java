package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.HotPost;
import com.javaweb.MichaelKai.service.HotPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 热帖表
 * @author: YuKai Fan
 * @create: 2019-07-01 17:05:29
 *
 */
@RestController
@RequestMapping("/api")
public class HotPostController {
    @Autowired
    private HotPostService hotPostService;

    /**
     * 添加
     * @param hotPost
     * @return
     */
    @PostMapping("/hotPost")
    public Result addHotPost(@RequestBody HotPost hotPost) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), hotPostService.addHotPost(hotPost));
    }

    /**
     * 编辑修改
     * @param hotPost
     * @return
     */
    @PutMapping("/hotPost")
    public Result editHotPostById(@RequestBody HotPost hotPost) {
        hotPostService.editHotPostById(hotPost);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), hotPost);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/hotPost")
    public Result delHotPostById(@RequestParam String id) {
        hotPostService.delHotPostById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/hotPosts/{ids}")
    public Result delHotPostByIds(@PathVariable("ids") String[] ids) {
        hotPostService.delHotPostByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getHotPosts/noPage")
    public Result getHotPosts(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = hotPostService.getHotPosts(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getHotPosts")
    public Result getUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = hotPostService.getHotPosts(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


}
