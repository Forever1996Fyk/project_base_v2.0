package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.mapper.DictItemMapper;
import com.javaweb.MichaelKai.mapper.DictMapper;
import com.javaweb.MichaelKai.pojo.DictItem;
import com.javaweb.MichaelKai.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private DictMapper dictMapper;

    @Override
    public DictItem addDictItem(DictItem dictItem) {
        dictItem.setItemCode(dictItem.getItemCode().trim());
        dictItem.setItemName(dictItem.getItemName().trim());
        dictItem.setId(AppUtil.randomId());
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
    public PageInfo<Map<String, Object>> getDictItems(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getDictItems(map);
        PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getDictItems(Map<String, Object> map) {
        return dictItemMapper.getDictItems(map);
    }
}
