package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.ActionLog;
import com.javaweb.MichaelKai.service.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 用户日志
 * @author: YuKai Fan
 * @create: 2019-05-31 15:39:53
 *
 */
@RestController
@RequestMapping("/api")
public class ActionLogController {
    @Autowired
    private ActionLogService actionLogService;

    /**
     * 添加
     * @param actionLog
     * @return
     */
    @PostMapping("/actionLog")
    public Result addActionLog(@RequestBody ActionLog actionLog) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), actionLogService.addActionLog(actionLog));
    }

    /**
     * 编辑修改
     * @param actionLog
     * @return
     */
    @PutMapping("/actionLog")
    public Result editActionLogById(@RequestBody ActionLog actionLog) {
        actionLogService.editActionLogById(actionLog);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), actionLog);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/actionLog")
    public Result editActionLogById(@RequestParam String id) {
        actionLogService.delActionLogById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/actionLogs/{ids}")
    public Result editActionLogByIds(@PathVariable("ids") String[] ids) {
        actionLogService.delActionLogByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getActionLogs/noPage")
    public Result getActionLogs(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = actionLogService.getActionLogs(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getActionLogs")
    public Result getUsers(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = actionLogService.getActionLogs(start, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 清空日志
     * @return
     */
    @DeleteMapping("/delActionLogReals")
    public Result delActionLogReals() {
        actionLogService.delActionLogReals();
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }


}
