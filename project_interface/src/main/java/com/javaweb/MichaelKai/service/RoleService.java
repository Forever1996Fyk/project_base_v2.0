package com.javaweb.MichaelKai.service;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.pojo.Role;
import com.javaweb.MichaelKai.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 角色表
  * @author: YuKai Fan
  * @create: 2019-05-20 11:51:00
  **/
public interface RoleService {
	

   /**
     * 新增
     * @param role 实体
     * @return
     */
    Role addRole(Role role);

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
    void editRoleById(Role role);

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
    void delRoleById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delRoleByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getRoles(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getRoles(Map<String, Object> map);

  /**
   * 获取角色权限
   * @param id
   * @return
   */
    List<Map<String,Object>> getAllPermissionsByRoleId(String id);

  /**
   * 保存角色权限
   * @param roleVo
   */
  RoleVo saveRoleAuth(RoleVo roleVo);
 }
