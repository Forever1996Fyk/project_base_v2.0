package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.Permission;
import org.apache.ibatis.annotations.Param;


/**
 * 权限表
 * 
 * @author earl
 * @date 2019-05-23 10:45:55
 */
public interface PermissionMapper {



    /**
     * 新增
     * @param permission 实体
     * @return
     */
    int addPermission(Permission permission);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addPermissions(@Param(value = "list") List<Permission> list);

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
    int editPermissionById(Permission permission);

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
    int delPermissionById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delPermissionByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delPermissionRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delPermissionRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delPermissionReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getPermissions(Map<String, Object> map);

}
