package com.javaweb.MichaelKai.mapper;

import java.util.List;
import java.util.Map;
import com.javaweb.MichaelKai.pojo.HotPost;
import org.apache.ibatis.annotations.Param;


/**
 * 热帖表
 * 
 * @author YuKai Fan
 * @date 2019-07-01 17:05:29
 */
public interface HotPostMapper {



    /**
     * 新增
     * @param hotPost 实体
     * @return
     */
    int addHotPost(HotPost hotPost);

    /**
     * 批量新增
     * @param list 实体集合
     */
    void addHotPosts(@Param(value = "list") List<HotPost> list);

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
    int editHotPostById(HotPost hotPost);

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
    int delHotPostById(String id);

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delHotPostByIds(List<String> ids);

    /**
     * 真删除
     *
     * @param id 主键
     * @return dao成功失败标志
     */
    int delHotPostRealById(String id);

    /**
     * 真批量删除
     *
     * @param ids 主键集合
     * @return dao成功失败标志
     */
    int delHotPostRealByIds(List<String> ids);

    /**
     * 全部真删除
     * @return
     */
    int delHotPostReals();

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getHotPosts(Map<String, Object> map);

}
