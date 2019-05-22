package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.mapper.RoleMapper;
import com.javaweb.MichaelKai.pojo.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


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
	@Autowired
    private IdWorker idWorker;

    @Override
    public Role addRole(Role role) {
        role.setId(String.valueOf(idWorker.nextId()));
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
    public PageInfo<Map<String, Object>> getRoles(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        List<Map<String, Object>> list = this.getRoles(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getRoles(Map<String, Object> map) {
        return roleMapper.getRoles(map);
    }
}
