package com.javaweb.MichaelKai.elasticsearch.controller;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.elasticsearch.service.HotPostElasticService;
import com.javaweb.MichaelKai.pojo.HotPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-07-02 16:48
 **/
@RestController
@RequestMapping("/api")
public class HotPostElasticController {
    @Autowired
    private HotPostElasticService hotPostElasticService;

    @PostMapping("/elastic/add")
    public void add() throws Exception {
        List<Map<String, Object>> hotPosts = hotPostElasticService.getHotPosts(null);
        List<HotPost> list = new ArrayList<>();
        for (Map<String, Object> hotPost : hotPosts) {
            HotPost hotPostEntity = (HotPost) MapUtil.mapToObject(HotPost.class, hotPost);
            list.add(hotPostEntity);
        }
        hotPostElasticService.addHostPostsToES(list);
    }

    @GetMapping("/elastic/query")
    public Result query(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = hotPostElasticService.searchHotPost(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

}