package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.service.PermissionService;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 页面跳转controller
 * @author: YuKai Fan
 * @create: 2019-05-21 15:11
 **/
@Controller
@RequestMapping("/system")
public class PageController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    /**
     * 用户管理列表
     * @return
     */
    @RequestMapping("/user/userList")
    public String userList() {
        return "system/user/user";
    }

    /**
     * 用户添加页面
     * @return
     */
    @RequestMapping("/user/add")
    public String addUser() {
        return "system/user/add";
    }

    /**
     * 用户修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/user/edit")
    public String editUser(String id, Model model) {
        Map<String, Object> userById = userService.getUserById(id);
        model.addAttribute("user", userById);
        return "system/user/add";
    }

    @RequestMapping("/user/roleAssign/{ids}")
    public String roleAssign(@PathVariable("ids") String[] ids, Model model) {
        //获取所有的角色
        List<Map<String, Object>> roles = roleService.getRoles(null);

        //根据用户id获取该用户角色
        if (ids.length == 1) {
            List<Map<String, Object>> allRolesByUserId = userService.getAllRolesByUserId(ids[0]);
            if (roles.size() > 0) {
                for (Map<String, Object> roleMap : roles) {
                    if (allRolesByUserId.contains(roleMap)) {
                        roleMap.put("selected", 1);
                    } else {
                        roleMap.put("selected", 0);
                    }
                }

                Map<String, Object> roleList = new HashMap<>();
                roleList.put("userId", ids[0]);
                roleList.put("roleList", roles);

                model.addAttribute("roleList", roleList);
            }
        }

        return "system/user/roleAssign";
    }

    /**
     * 角色列表
     * @return
     */
    @RequestMapping("/role/roleList")
    public String roleList() {
        return "system/role/role";
    }

    /**
     * 角色添加
     * @return
     */
    @RequestMapping("/role/add")
    public String addRole() {
        return "system/role/add";
    }

    /**
     * 角色修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/role/edit")
    public String editRole(String id, Model model) {
        Map<String, Object> roleById = roleService.getRoleById(id);
        model.addAttribute("role", roleById);
        return "system/role/add";
    }

    /**
     * 分配权限
     * @param ids
     * @param model
     * @return
     */
    @RequestMapping("/role/auth/{ids}")
    public String roleAuth(@PathVariable("ids") String[] ids, Model model) {
        if (ids.length == 1) {
            model.addAttribute("id", ids[0]);
        }
        return "system/role/auth";
    }

    /**
     * 权限列表
     * @return
     */
    @RequestMapping("/permission/permissionList")
    public String permissionList() {
        return "system/permission/permission";
    }

    /**
     * 权限添加
     * @return
     */
    @GetMapping({"/permission/add", "/permission/add/{pid}"})
    public String addPermission(@PathVariable(value = "pid", required = false) String pid, Model model) {
        if (pid != null) {
            Map<String, Object> permission = permissionService.getPermissionById(pid);
        }
        return "system/permission/add";
    }

    /**
     * 权限修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/permission/edit/{id}")
    public String editPermission(@PathVariable(value = "id") String id, Model model) {
        Map<String, Object> permission = permissionService.getPermissionById(id);
        Map<String, Object> pPermission = permissionService.getPermissionById(permission.get("pid").toString());
        model.addAttribute("permission", permission);
        model.addAttribute("pPermission", pPermission);
        return "system/permission/add";
    }
}