package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.pojo.RolePermission;
import org.apache.ibatis.annotations.Param;


/**
 * 角色表
 * 
 * @author earl
 * @date 2019-05-20 11:51:00
 */
public interface RoleMapper {



    /**
     * 新增
     * @param role 实体
     * @return
     */
    int addRole(Role role);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addRoles(@Param(value = "list") List<Role> list);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getRoleById(String id);

    /**
     * 根据id修改
     * @param role 实体
     * @return
     */
    int editRoleById(Role role);

    /**
     * 批量修改
     *
     * @param role 实体
     * @param ids 主键集合
     */
    void editRoleByIds(@Param("map") Role role, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delRoleById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delRoleByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delRoleRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delRoleRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delRoleReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getRoles(Map<String, Object> map);

    /**
     * 获取角色权限
     * @param roleId
     * @return
     */
    List<Map<String,Object>> getAllPermissionsByRoleId(String roleId);

    /**
     * 根据roleId删除角色权限
     * @param roleId
     */
    void delPermissonsByRoleId(String roleId);

    /**
     * 添加角色权限
     * @param list
     */
    void addRolePermission(@Param(value = "list")List<Map<String, Object>> list);
}
