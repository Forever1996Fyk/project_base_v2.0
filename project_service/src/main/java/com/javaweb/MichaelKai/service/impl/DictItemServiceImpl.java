package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.IdWorker;
import com.javaweb.MichaelKai.mapper.DictMapper;
import com.javaweb.MichaelKai.thymeleaf.util.DictUtil;
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
    private DictMapper dictMapper;
	@Autowired
    private IdWorker idWorker;

    @Override
    public DictItem addDictItem(DictItem dictItem) {
        dictItem.setItemCode(dictItem.getItemCode().trim());
        dictItem.setItemName(dictItem.getItemName().trim());
        dictItem.setId(String.valueOf(idWorker.nextId()));
        dictItem.setStatus(StatusEnum.Normal.getValue());
        dictItemMapper.addDictItem(dictItem);

        clearCache(dictItem.getDicId());

        return dictItem;
    }

    @Override
    public Map<String, Object> getDictItemById(String id) {
        return dictItemMapper.getDictItemById(id);
    }

    @Override
    public void editDictItemById(DictItem dictItem) {
        dictItemMapper.editDictItemById(dictItem);

        Map<String, Object> dictItemById = dictItemMapper.getDictItemById(dictItem.getId());
        clearCache(dictItemById.get("dicId").toString());
    }

    @Override
    public void editDictItemByIds(DictItem dictItem, List<String> ids) {

    }

    @Override
    public void delDictItemById(String id) {
        dictItemMapper.delDictItemById(id);

        Map<String, Object> dictItemById = dictItemMapper.getDictItemById(id);
        clearCache(dictItemById.get("dicId").toString());
    }

    @Override
    public void delDictItemByIds(List<String> ids) {
        dictItemMapper.delDictItemByIds(ids);

        if(ids.size() > 0) {
            Map<String, Object> dictItemById = dictItemMapper.getDictItemById(ids.get(0));
            clearCache(dictItemById.get("dicId").toString());
        }
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

     /**
      * 清除数据字典缓存
      * @param dictId
      */
    private void clearCache(String dictId) {
        Map<String, Object> dict = dictMapper.getDictById(dictId);
        if (!"".equals(dict.get("dicCode")) && dict.get("dicCode") != null) {
            //清除数据字典缓存
            DictUtil.clearCache(dict.get("dicCode").toString());
        }
    }
}
