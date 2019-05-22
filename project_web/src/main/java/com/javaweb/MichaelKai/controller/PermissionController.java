package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.Permission;
import com.javaweb.MichaelKai.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 权限表
 * @author: YuKai Fan
 * @create: 2019-05-20 15:32:55
 *
 */
@RestController
@RequestMapping("/api")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 添加
     * @param permission
     * @return
     */
    @PostMapping("/permission")
    public Result addPermission(@RequestBody Permission permission) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), permissionService.addPermission(permission));
    }

    /**
     * 编辑修改
     * @param permission
     * @return
     */
    @PutMapping("/permission")
    public Result editPermissionById(@RequestBody Permission permission) {
        permissionService.editPermissionById(permission);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), permission);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/permission")
    public Result editPermissionById(@RequestParam String id) {
        permissionService.delPermissionById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/permissions/{ids}")
    public Result editPermissionByIds(@PathVariable("ids") String[] ids) {
        permissionService.delPermissionByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getPermissions/noPage")
    public Result getPermissions(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = permissionService.getPermissions(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getPermissions")
    public Result getUsers(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "length", required = false, defaultValue = "0") int pageSize,
                           @RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = permissionService.getPermissions(start, pageSize, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }


}
