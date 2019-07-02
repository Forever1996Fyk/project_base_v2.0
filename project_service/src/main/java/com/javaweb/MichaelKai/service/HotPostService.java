package com.javaweb.MichaelKai.service;

import com.javaweb.MichaelKai.pojo.HotPost;
import org.apache.ibatis.annotations.Param;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

 /**
  * @program: project_base
  * @description: 热帖表
  * @author: YuKai Fan
  * @create: 2019-07-01 17:05:29
  **/
public interface HotPostService {
	

   /**
     * 新增
     * @param hotPost 实体
     * @return
     */
    HotPost addHotPost(HotPost hotPost);

    /**
     * 根据id查询
     * @param id  主键
     * @return
     */
    Map<String, Object> getHotPostById(String id);

    /**
     * 根据id修改
     * @param hotPost 实体
     * @return
     */
    void editHotPostById(HotPost hotPost);

    /**
     * 批量修改
     *
     * @param hotPost 实体
     * @param ids 主键集合
     */
    void editHotPostByIds(@Param("map") HotPost hotPost, @Param("list") List<String> ids);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    void delHotPostById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    void delHotPostByIds(List<String> ids);

    /**
     * 获取所有(分页)
     * @param start 开始记录
     * @param pageSize 分页大小
     * @param map 参数
     * @return
     */
    PageInfo<Map<String, Object>> getHotPosts(int start, int pageSize, Map<String, Object> map);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getHotPosts(Map<String, Object> map);
}
