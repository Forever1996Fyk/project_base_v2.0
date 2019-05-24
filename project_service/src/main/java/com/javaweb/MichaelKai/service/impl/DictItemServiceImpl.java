package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.DictItemService;
import com.javaweb.MichaelKai.mapper.DictItemMapper;
import com.javaweb.MichaelKai.pojo.DictItem;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 数据字典项
  * @author: YuKai Fan
  * @create: 2019-05-24 16:13:29
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class DictItemServiceImpl implements  DictItemService {

	@Autowired
	private DictItemMapper dictItemMapper;
	@Autowired
    private IdWorker idWorker;

    @Override
    public DictItem addDictItem(DictItem dictItem) {
        dictItem.setId(String.valueOf(idWorker.nextId()));
                                                                                                                                                            dictItem.setStatus(StatusEnum.Normal.getValue());
                                                                                                            dictItemMapper.addDictItem(dictItem);
        return dictItem;
    }

    @Override
    public Map<String, Object> getDictItemById(String id) {
        return dictItemMapper.getDictItemById(id);
    }

    @Override
    public void editDictItemById(DictItem dictItem) {
        dictItemMapper.editDictItemById(dictItem);
    }

    @Override
    public void editDictItemByIds(DictItem dictItem, List<String> ids) {

    }

    @Override
    public void delDictItemById(String id) {
        dictItemMapper.delDictItemById(id);
    }

    @Override
    public void delDictItemByIds(List<String> ids) {
        dictItemMapper.delDictItemByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getDictItems(int start, int pageSize, Map<String, Object> map) {
        PageHelper.offsetPage(start, pageSize);
        List<Map<String, Object>> list = this.getDictItems(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getDictItems(Map<String, Object> map) {
        return dictItemMapper.getDictItems(map);
    }
}
