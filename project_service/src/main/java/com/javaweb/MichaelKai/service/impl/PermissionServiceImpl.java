package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.PermissionService;
import com.javaweb.MichaelKai.mapper.PermissionMapper;
import com.javaweb.MichaelKai.pojo.Permission;

import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 权限表
  * @author: YuKai Fan
  * @create: 2019-05-20 15:32:55
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class PermissionServiceImpl implements  PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
    private IdWorker idWorker;

    @Override
    public Permission addPermission(Permission permission) {
        permission.setId(String.valueOf(idWorker.nextId()));
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
    public List<Map<String, Object>> getPermissions(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        return this.getPermissions(map);
    }

    @Override
    public List<Map<String, Object>> getPermissions(Map<String, Object> map) {
        return permissionMapper.getPermissions(map);
    }
}
