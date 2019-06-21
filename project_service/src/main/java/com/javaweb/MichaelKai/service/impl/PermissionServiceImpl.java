package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.mapper.PermissionMapper;
import com.javaweb.MichaelKai.pojo.Permission;
import com.javaweb.MichaelKai.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 权限表
  * @author: YuKai Fan
  * @create: 2019-05-23 10:45:55
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class PermissionServiceImpl implements  PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;

    @Override
    public Permission addPermission(Permission permission) {
        permission.setId(AppUtil.randomId());
        permission.setStatus(StatusEnum.Normal.getValue());
        permissionMapper.addPermission(permission);
        return permission;
    }

    @Override
    public Map<String, Object> getPermissionById(String id) {
        return permissionMapper.getPermissionById(id);
    }

    @Override
    public void editPermissionById(Permission permission) {
        permissionMapper.editPermissionById(permission);
    }

    @Override
    public void editPermissionByIds(Permission permission, List<String> ids) {

    }

    @Override
    public void delPermissionById(String id) {
        permissionMapper.delPermissionById(id);
    }

    @Override
    public void delPermissionByIds(List<String> ids) {
        permissionMapper.delPermissionByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getPermissions(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getPermissions(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getPermissions(Map<String, Object> map) {
        return permissionMapper.getPermissions(map);
    }
}
