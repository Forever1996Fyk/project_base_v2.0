package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.DictService;
import com.javaweb.MichaelKai.mapper.DictMapper;
import com.javaweb.MichaelKai.pojo.Dict;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 数据字典
  * @author: YuKai Fan
  * @create: 2019-05-24 16:13:29
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class DictServiceImpl implements  DictService {

	@Autowired
	private DictMapper dictMapper;
	@Autowired
    private IdWorker idWorker;

    @Override
    public Dict addDict(Dict dict) {
        dict.setId(String.valueOf(idWorker.nextId()));
        dict.setStatus(StatusEnum.Normal.getValue());
        dictMapper.addDict(dict);
        return dict;
    }

    @Override
    public Map<String, Object> getDictById(String id) {
        return dictMapper.getDictById(id);
    }

    @Override
    public void editDictById(Dict dict) {
        dictMapper.editDictById(dict);
    }

    @Override
    public void editDictByIds(Dict dict, List<String> ids) {

    }

    @Override
    public void delDictById(String id) {
        dictMapper.delDictById(id);
    }

    @Override
    public void delDictByIds(List<String> ids) {
        dictMapper.delDictByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getDicts(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        List<Map<String, Object>> list = this.getDicts(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getDicts(Map<String, Object> map) {
        return dictMapper.getDicts(map);
    }
}
