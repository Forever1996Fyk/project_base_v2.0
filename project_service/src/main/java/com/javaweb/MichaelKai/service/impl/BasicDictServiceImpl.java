package com.javaweb.MichaelKai.service.impl;

import com.javaweb.MichaelKai.mapper.BasicDictMapper;
import com.javaweb.MichaelKai.service.BasicDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 16:24
 **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class BasicDictServiceImpl implements BasicDictService {
    @Autowired
    private BasicDictMapper basicDictMapper;

    @Override
    public List<Map<String, Object>> getDictItem(Map<String, Object> map) {
        return basicDictMapper.getDictItemsByParam(map);
    }
}