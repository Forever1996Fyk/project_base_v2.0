package com.javaweb.MichaelKai.elasticsearch.service.impl;

import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.elasticsearch.service.HotPostElasticService;
import com.javaweb.MichaelKai.mapper.HotPostMapper;
import com.javaweb.MichaelKai.pojo.HotPost;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-07-02 15:35
 **/
@Slf4j
@Service
public class HotPostElasticServiceImpl implements HotPostElasticService {
    @Autowired
    private HotPostMapper hotPostMapper;

    @Autowired
    private JestClient jestClient;


    @Override
    public void addHostPostToES(HotPost hotPost) {
        Bulk.Builder bulk = new Bulk.Builder();
        Index index = new Index.Builder(hotPost).index(HotPost.INDEX).type(HotPost.ORDER_TYPE).build();
        bulk.addAction(index);

        try {
            jestClient.execute(bulk.build());
            log.info("ES 插入完成");
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @Override
    public void addHostPostsToES(List<HotPost> list) {
        for (HotPost hotPost : list) {
            addHostPostToES(hotPost);
        }
    }

    @Override
    public List<Map<String, Object>> getHotPosts(Map<String, Object> map) {
        return hotPostMapper.getHotPosts(map);
    }

    @Override
    public List<Map<String, Object>> searchHotPost(Map<String, Object> map) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(map) && map.containsKey("keyword")) {
            Object keyword = map.get("keyword");
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("title", keyword)).boost(5);
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("content", keyword)).boost(4);
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("type", keyword)).boost(3);
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("sort", keyword)).boost(2);
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("remark", keyword)).boost(1);
            boolQueryBuilder.should(QueryBuilders.commonTermsQuery("status", keyword)).boost(0);
        }

        //高亮操作
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("content");
        highlightBuilder.field("title");
        highlightBuilder.preTags("<font color='red'>").postTags("</font>");
        highlightBuilder.fragmentSize(500);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder).size(10);
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(HotPost.INDEX).addType(HotPost.ORDER_TYPE).build();


        try {
            SearchResult result = jestClient.execute(search);
            System.out.println("本次查询共查到：" + result.getTotal());
            List<SearchResult.Hit<HotPost, Void>> hits = result.getHits(HotPost.class);
            System.out.println(hits.size());
            List<Map<String, Object>> list = new ArrayList<>();

            for (SearchResult.Hit<HotPost, Void> hit : hits) {
                HotPost source = hit.source;
                //获取高亮后的内容
                Map<String, List<String>> highlight = hit.highlight;
                if (!CollectionUtils.isEmpty(highlight)) {
                    List<String> title = highlight.get("title");//高亮后的title
                    if (!CollectionUtils.isEmpty(title)) {
                        source.setTitle(title.get(0));
                    }

                    List<String> content = highlight.get("content");//高亮后的content
                    if (!CollectionUtils.isEmpty(content)) {
                        source.setContent(content.get(0));
                    }
                }

                /*HotPost hotPost = new HotPost();
                hotPost.setTitle(source.getTitle());
                hotPost.setContent(source.getContent());
                hotPost.setId(source.getId());
                hotPost*/
                HotPost hotPost = new HotPost();
                BeanUtils.copyProperties(source, hotPost);
                list.add((Map<String, Object>) MapUtil.objectToMap(hotPost));
            }

            return list;

        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> searchHotPost(Map<String, Object> map, int start, int pageSize) {
        //分页参数

        return null;
    }
}