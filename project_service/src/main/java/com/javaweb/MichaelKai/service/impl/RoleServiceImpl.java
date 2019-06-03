package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.mapper.RoleMapper;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.pojo.RolePermission;
import com.javaweb.MichaelKai.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
  * @program: project_base
  * @description: 角色表
  * @author: YuKai Fan
  * @create: 2019-05-22 16:02:07
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class RoleServiceImpl implements  RoleService {

	@Autowired
	private RoleMapper roleMapper;

    @Override
    public Role addRole(Role role) {
        role.setId(AppUtil.randomId());
        role.setStatus(StatusEnum.Normal.getValue());
        roleMapper.addRole(role);
        return role;
    }

     @Override
    public Map<String, Object> getRoleById(String id) {
        return roleMapper.getRoleById(id);
    }

    @Override
    public void editRoleById(Role role) {
        roleMapper.editRoleById(role);
    }

    @Override
    public void editRoleByIds(Role role, List<String> ids) {

    }

    @Override
    public void delRoleById(String id) {
        roleMapper.delRoleById(id);
    }

    @Override
    public void delRoleByIds(List<String> ids) {
        roleMapper.delRoleByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getRoles(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getRoles(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getRoles(Map<String, Object> map) {
        return roleMapper.getRoles(map);
    }

     @Override
     public List<Map<String, Object>> getAllPermissionsByRoleId(String id) {
         return roleMapper.getAllPermissionsByRoleId(id);
     }

     @Override
     public RolePermission saveRoleAuth(RolePermission rolePermission) {
         //先删除该角色的权限
         roleMapper.delPermissonsByRoleId(rolePermission.getRoleId());

         //再新增权限
         Set<String> permissionIds = rolePermission.getPermissionIds();
         if (!CollectionUtils.isEmpty(permissionIds)) {
             List<Map<String, Object>> list = new ArrayList<>();
             for (String permissionId : permissionIds) {
                 Map<String, Object> map = new HashMap<>();
                 map.put("roleId", rolePermission.getRoleId());
                 map.put("permissionId", permissionId);

                 list.add(map);
             }
             roleMapper.addRolePermission(list);
         }

         return rolePermission;
     }
 }
