package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import com.javaweb.MichaelKai.mapper.PermissionMapper;
import com.javaweb.MichaelKai.pojo.Permission;
import com.javaweb.MichaelKai.service.PermissionService;
import com.javaweb.MichaelKai.thymeleaf.util.DictUtil;
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
	@Autowired
    private IdWorker idWorker;

    @Override
    public Permission addPermission(Permission permission) {
        permission.setId(String.valueOf(idWorker.nextId()));
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
    public PageInfo<Map<String, Object>> getPermissions(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        List<Map<String, Object>> list = this.getPermissions(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getPermissions(Map<String, Object> map) {
        List<Map<String, Object>> permissions = permissionMapper.getPermissions(map);
        for (Map<String, Object> permission : permissions) {
            permission.put("menuTypeName", DictUtil.keyValue("MENU_TYPE", permission.get("level").toString()));
            permission.put("statusName", DictUtil.keyValue("STATUS_TYPE", permission.get("status").toString()));
        }
        return permissionMapper.getPermissions(map);
    }
}
