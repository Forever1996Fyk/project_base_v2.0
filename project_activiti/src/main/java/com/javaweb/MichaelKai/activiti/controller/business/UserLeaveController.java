package com.javaweb.MichaelKai.activiti.controller.business;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserLeave;
import com.javaweb.MichaelKai.service.UserLeaveService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 请假流程表
 * @author: YuKai Fan
 * @create: 2019-08-08 10:30:23
 *
 */
@RestController
@RequestMapping("/api/activiti")
public class UserLeaveController {
    @Autowired
    private UserLeaveService userLeaveService;

    /**
     * 新建请假流程
     * @param userLeave
     * @return
     */
    @PostMapping("/userLeave")
    public Result addUserLeave(@RequestBody UserLeave userLeave) {
        User user = ShiroKit.getUser();
        userLeave.setUserId(user.getId());
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), userLeaveService.addUserLeave(userLeave));
    }

    /**
     * 编辑修改
     * @param userLeave
     * @return
     */
    @PutMapping("/userLeave")
    public Result editUserLeaveById(@RequestBody UserLeave userLeave) {
        userLeaveService.editUserLeaveById(userLeave);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), userLeave);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/userLeave")
    public Result delUserLeaveById(@RequestParam String id) {
        userLeaveService.delUserLeaveById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/userLeaves/{ids}")
    public Result delUserLeaveByIds(@PathVariable("ids") String[] ids) {
        userLeaveService.delUserLeaveByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getUserLeaves/noPage")
    public Result getUserLeaves(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = userLeaveService.getUserLeaves(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getUserLeaves")
    public Result getUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        User user = ShiroKit.getUser();
        map.put("userId", user.getId());
        PageInfo<Map<String, Object>> pageList = userLeaveService.getUserLeaves(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


}
