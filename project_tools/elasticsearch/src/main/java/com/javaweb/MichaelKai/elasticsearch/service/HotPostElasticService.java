package com.javaweb.MichaelKai.elasticsearch.service;

import com.javaweb.MichaelKai.pojo.HotPost;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 热帖es搜索服务
 * @author: YuKai Fan
 * @create: 2019-07-02 15:26
 **/
public interface HotPostElasticService {

    /**
     * 保存热帖到ES
     * @param hotPost
     */
    void addHostPostToES(HotPost hotPost);

    /**
     * 批量保存到ES
     * @param list
     */
    void addHostPostsToES(List<HotPost> list);

    /**
     * 获取所有数据.
     * @param map 页面表单
     * @return 结果集合
     */
    List<Map<String, Object>> getHotPosts(Map<String, Object> map);

    /**
     * 根据参数搜索复合的热帖
     * @param map
     * @return
     */
    List<Map<String, Object>> searchHotPost(Map<String, Object> map);

    /**
     * 根据参数搜索复合的热帖
     * @param map
     * @return
     */
    List<Map<String, Object>> searchHotPost(Map<String, Object> map, int start, int pageSize);
}