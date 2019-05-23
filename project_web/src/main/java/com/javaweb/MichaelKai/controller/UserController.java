package com.javaweb.MichaelKai.controller;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserRole;
import com.javaweb.MichaelKai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 用户controller层
 * @author: YuKai Fan
 * @create: 2019-05-17 14:58
 **/
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 添加用户
     * @param user
     * @returneducation
     */
    @PostMapping("/user")
    public Result addUser(@RequestBody User user) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), userService.addUser(user));
    }

    /**
     * 编辑修改用户
     * @param user
     * @return
     */
    @PutMapping("/user")
    public Result editUserById(@RequestBody User user) {
        userService.editUserById(user);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), user);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/user")
    public Result editUserById(@RequestParam String id) {
        userService.delUserById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id删除用户
     * @param ids
     * @return
     */
    @DeleteMapping("/users/{ids}")
    public Result editUserByIds(@PathVariable("ids") String[] ids) {
        userService.delUserByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有的用户(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getUsers/noPage")
    public Result getUsers(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = userService.getUsers(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有用户
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getUsers")
    public Result getUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = userService.getUsers(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 分配角色
     * @param userRole
     * @return
     */
    @PostMapping("/user/roleAssign")
    public Result roleAssign(@RequestBody UserRole userRole) {
        userService.roleAssign(userRole);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

}