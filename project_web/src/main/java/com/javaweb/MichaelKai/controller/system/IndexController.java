package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.common.constants.AdminConstant;
import com.javaweb.MichaelKai.common.enums.MenuTypeEnum;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.pojo.Permission;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.PermissionService;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 主页面controller
 * @author: YuKai Fan
 * @create: 2019-05-21 10:40
 **/
@Controller
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 后台首页
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) throws Exception {

        //获取当前用户
        User user = (User) ShiroKit.getUser();

        Map<String, Permission> keyMenu = new HashMap<>();
        try {
            //如果是超级管理员，则实时更新菜单
            if (user.getId().equals(AdminConstant.ADMIN_ID)) {
                List<Map<String, Object>> permissions = permissionService.getPermissions(null);
                for (Map<String, Object> permissionMap : permissions) {
                    Permission permission = (Permission) MapUtil.mapToObject(Permission.class, permissionMap);
                    keyMenu.put(permission.getId(), permission);
                }
            } else {//其他用户从相应的角色中获取菜单
                List<Map<String, Object>> allPermissionsByUserId = userService.getAllPermissionsByUserId(user.getId());
                for (Map<String, Object> permissionMap : allPermissionsByUserId) {
                    Permission permission = (Permission) MapUtil.mapToObject(Permission.class, permissionMap);
                    keyMenu.put(permission.getId(), permission);
                }
            }

            //封装菜单树形数据
            Map<String, Object> treeMap = new HashMap<>();
            for (Map.Entry<String, Permission> entry : keyMenu.entrySet()) {
                Permission permission = entry.getValue();
                if (!permission.getLevel().equals(MenuTypeEnum.NOT_MENU.getLevel())) {
                    if (keyMenu.get(permission.getPid()) != null) {//如果pid不为空,就以排序号为key, permission为值
                        keyMenu.get(permission.getPid()).getChildren().put(String.valueOf(permission.getSort()), permission);
                    } else {
                        if (permission.getLevel().equals(MenuTypeEnum.TOP_LEVEL.getLevel())) {//如果是一级菜单
                            treeMap.put(String.valueOf(permission.getSort()), permission);
                        }
                    }
                } else {
                    if (keyMenu.get(permission.getPid()) != null) {//如果pid不为空,就以排序号为key, permission为值
                        keyMenu.get(permission.getPid()).getChildren().put(String.valueOf(permission.getSort()), permission);
                    }
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("treeMenu", treeMap);

            return "index";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}