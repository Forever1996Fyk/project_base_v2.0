package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.Permission;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 权限表
  * @author: YuKai Fan
  * @create: 2019-05-23 10:45:55
  **/
public interface PermissionService {
	

   /**
     * 新增
     * @param permission 实体
     * @return
     */
    Permission addPermission(Permission permission);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getPermissionById(String id);

    /**
     * 根据id修改
     * @param permission 实体
     * @return
     */
    void editPermissionById(Permission permission);

    /**
     * 批量修改
     *
     * @param permission 实体
     * @param ids 主键集合
     */
    void editPermissionByIds(@Param("map") Permission permission, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void delPermissionById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delPermissionByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getPermissions(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getPermissions(Map<String, Object> map);
}
