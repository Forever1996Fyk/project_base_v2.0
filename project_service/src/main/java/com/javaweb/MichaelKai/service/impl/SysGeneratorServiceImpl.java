package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.code.createCode.GenUtils;
import com.javaweb.MichaelKai.mapper.SysGeneratorMapper;
import com.javaweb.MichaelKai.service.SysGeneratorService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-05-24 08:57
 **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class SysGeneratorServiceImpl implements SysGeneratorService {
    @Autowired
    private SysGeneratorMapper sysGeneratorMapper;

    @Override
    public PageInfo<Map<String, Object>> pageList(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = sysGeneratorMapper.pageList(map);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo <>(list);
        return pageInfo;
    }

    @Override
    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorMapper.queryTable(tableName);
    }

    @Override
    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorMapper.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for(String tableName : tableNames){
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}