package com.javaweb.MichaelKai.activiti.service;

import com.javaweb.MichaelKai.pojo.ProcessClass;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 流程分类表
  * @author: YuKai Fan
  * @create: 2019-08-12 10:14:44
  **/
public interface ProcessClassService {
	

   /**
     * 新增
     * @param request 实体
     * @param userId 当前用户id
     * @return
     */
    ProcessClass addProcessClass(HttpServletRequest request, String userId) throws Exception;

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getProcessClassById(String id);

    /**
     * 根据id修改
     * @param request 实体
     * @return
     */
    void editProcessClassById(HttpServletRequest request, String userId) throws Exception;

    /**
     * 批量修改
     *
     * @param processClass 实体
     * @param ids 主键集合
     */
    void editProcessClassByIds(@Param("map") ProcessClass processClass, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void delProcessClassById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delProcessClassByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getProcessClasss(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getProcessClasss(Map<String, Object> map);
}
