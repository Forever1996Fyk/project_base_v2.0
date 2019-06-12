package com.javaweb.MichaelKai.controller;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.service.PermissionService;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 角色表
 * @author: YuKai Fan
 * @create: 2019-05-22 16:02:07
 *
 */
@RestController
@RequestMapping("/api")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 添加
     * @param role
     * @return
     */
    @PostMapping("/role")
    public Result addRole(@RequestBody Role role) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), roleService.addRole(role));
    }

    /**
     * 编辑修改
     * @param role
     * @return
     */
    @PutMapping("/role")
    public Result editRoleById(@RequestBody Role role) {
        roleService.editRoleById(role);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), role);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/role")
    public Result editRoleById(@RequestParam String id) {
        roleService.delRoleById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/roles/{ids}")
    public Result editRoleByIds(@PathVariable("ids") String[] ids) {
        roleService.delRoleByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getRoles/noPage")
    public Result getRoles(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = roleService.getRoles(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param start 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getRoles")
    public Result getUsers(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        PageInfo<Map<String, Object>> pageList = roleService.getRoles(start, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 获取角色权限
     * @param id
     * @return
     */
    @GetMapping("/role/authList")
    public Result getRoles(@RequestParam String id) {
        //获取所有权限
        Map<String, Object> map = new HashMap<>();
        map.put("flag", "1");
        List<Map<String, Object>> permissions = permissionService.getPermissions(map);

        if (permissions.size() > 0) {
            //根据roleId获取该角色的权限
            List<Map<String, Object>> allPermissionsByRoleId = roleService.getAllPermissionsByRoleId(id);
            //判断是否包含
            for (Map<String, Object> permission : permissions) {
                if (allPermissionsByRoleId.contains(permission)) {
                    permission.put("selected", 1);
                } else {
                    permission.put("selected", 0);
                }
            }
        }

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), permissions);
    }

    /**
     * 保存角色权限
     * @return
     */
    @PostMapping("/role/saveAuth")
    public Result saveRoleAuth(@RequestBody RoleVo roleVo) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "保存" + ResultEnum.SUCCESS.getMessage(), roleService.saveRoleAuth(roleVo));
    }



}
