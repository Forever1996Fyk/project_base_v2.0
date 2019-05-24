package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.common.utils.HttpServletUtil;
import com.javaweb.MichaelKai.service.*;
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
    @Autowired
    private SysGeneratorService sysGeneratorService;
    @Autowired
    private DictService dictService;
    @Autowired
    private DictItemService dictItemService;
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
    public String permissionList(Model model) {
        String search = HttpServletUtil.getRequest().getQueryString();
        model.addAttribute("search", search);
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

    /**
     * 代码生成页面(数据库表，字段列表)
     * @return
     */
    @RequestMapping("/generator/code")
    public String generatorTableColumn() {
        return "system/generator/tableColumn";
    }

    /**
     * 代码生成页面(数据库表，字段列表)
     * @return
     */
    @RequestMapping("/generator/columnsView/{tableName}")
    public String columnsView(@PathVariable(value = "tableName") String tableName, Model model) {
        List<Map<String, String>> list = sysGeneratorService.queryColumns(tableName);
        model.addAttribute("list", list);
        model.addAttribute("tableName", tableName);
        return "system/generator/columnsView";
    }

    /**
     * 数据字典管理列表
     * @return
     */
    @RequestMapping("/basicDict/dictList")
    public String dictList() {
        return "system/basicDict/dict";
    }

    /**
     * 数据字典添加页面
     * @return
     */
    @RequestMapping("/basicDict/addDict")
    public String addDict() {
        return "system/basicDict/addDict";
    }

    /**
     * 数据字典修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/basicDict/editDict")
    public String editDict(String id, Model model) {
        Map<String, Object> dictById = dictService.getDictById(id);
        model.addAttribute("dict", dictById);
        return "system/basicDict/addDict";
    }

    /**
     * 根据dic_id获取数据字典项
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/basicDict/dictItemsView")
    public String dictItemsView(String id, Model model) {
        Map<String, Object> dictById = dictService.getDictById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("dic_id", id);
        List<Map<String, Object>> dictItems = dictItemService.getDictItems(map);

        model.addAttribute("dict", dictById);
        model.addAttribute("list", dictItems);
        return "system/basicDict/dictItem";
    }

    /**
     * 数据字典添加页面
     * @return
     */
    @RequestMapping("/basicDict/addDictItem/{dicId}")
    public String addDictItem(@PathVariable(value = "dicId") String dicId, Model model) {
        model.addAttribute("dicId", dicId);
        return "system/basicDict/addDictItem";
    }

    /**
     * 数据字典修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/basicDict/editDictItem")
    public String editDictItem(String id, Model model) {
        Map<String, Object> dictItemById = dictItemService.getDictItemById(id);
        model.addAttribute("dictItem", dictItemById);
        return "system/basicDict/addDictItem";
    }
}