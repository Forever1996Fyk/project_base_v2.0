package com.javaweb.MichaelKai.activiti.controller.business;

import com.javaweb.MichaelKai.activiti.service.ProcessClassService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.ProcessClass;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 流程分类表
 * @author: YuKai Fan
 * @create: 2019-08-12 10:14:44
 *
 */
@RestController
@RequestMapping("/api")
public class ProcessClassController {
    @Autowired
    private ProcessClassService processClassService;

    /**
     * 添加
     * @param request
     * @return
     */
    @PostMapping("/processClass")
    public Result addProcessClass(HttpServletRequest request) throws Exception {
        User user = ShiroKit.getUser();
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), processClassService.addProcessClass(request, user.getId()));
    }

    /**
     * 编辑修改
     * @param request
     * @return
     */
    @PostMapping("/editProcessClass")
    public Result editProcessClassById(HttpServletRequest request) throws Exception {
        User user = ShiroKit.getUser();
        processClassService.editProcessClassById(request, user.getId());
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/processClass")
    public Result delProcessClassById(@RequestParam String id) {
        processClassService.delProcessClassById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/processClasss/{ids}")
    public Result delProcessClassByIds(@PathVariable("ids") String[] ids) {
        processClassService.delProcessClassByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getProcessClasss/noPage")
    public Result getProcessClasss(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = processClassService.getProcessClasss(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getProcessClasss")
    public Result getUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = processClassService.getProcessClasss(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


}
