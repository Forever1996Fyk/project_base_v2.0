package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.HotPostService;
import com.javaweb.MichaelKai.mapper.HotPostMapper;
import com.javaweb.MichaelKai.pojo.HotPost;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 热帖表
  * @author: YuKai Fan
  * @create: 2019-07-01 17:05:29
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class HotPostServiceImpl implements  HotPostService {

	@Autowired
	private HotPostMapper hotPostMapper;

    @Override
    public HotPost addHotPost(HotPost hotPost) {
        hotPost.setId(AppUtil.randomId());
        hotPost.setStatus(StatusEnum.Normal.getValue());
        hotPostMapper.addHotPost(hotPost);
        return hotPost;
    }

    @Override
    public Map<String, Object> getHotPostById(String id) {
        return hotPostMapper.getHotPostById(id);
    }

    @Override
    public void editHotPostById(HotPost hotPost) {
        hotPostMapper.editHotPostById(hotPost);
    }

    @Override
    public void editHotPostByIds(HotPost hotPost, List<String> ids) {

    }

    @Override
    public void delHotPostById(String id) {
        hotPostMapper.delHotPostById(id);
    }

    @Override
    public void delHotPostByIds(List<String> ids) {
        hotPostMapper.delHotPostByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getHotPosts(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getHotPosts(map);
        PageInfo<Map<String, Object>> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getHotPosts(Map<String, Object> map) {
        return hotPostMapper.getHotPosts(map);
    }
}
