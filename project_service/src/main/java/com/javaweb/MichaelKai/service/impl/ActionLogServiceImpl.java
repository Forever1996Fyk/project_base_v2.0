package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.thymeleaf.util.DictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.ActionLogService;
import com.javaweb.MichaelKai.mapper.ActionLogMapper;
import com.javaweb.MichaelKai.pojo.ActionLog;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


 /**
  * @program: project_base
  * @description: 用户日志
  * @author: YuKai Fan
  * @create: 2019-05-31 15:39:53
  **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class ActionLogServiceImpl implements  ActionLogService {

	@Autowired
	private ActionLogMapper actionLogMapper;

    @Override
    public ActionLog addActionLog(ActionLog actionLog) {
        actionLog.setId(AppUtil.randomId());
                                                                                                                                                                                                                                                                                    actionLogMapper.addActionLog(actionLog);
        return actionLog;
    }

    @Override
    public Map<String, Object> getActionLogById(String id) {
        return actionLogMapper.getActionLogById(id);
    }

    @Override
    public void editActionLogById(ActionLog actionLog) {
        actionLogMapper.editActionLogById(actionLog);
    }

    @Override
    public void editActionLogByIds(ActionLog actionLog, List<String> ids) {

    }

    @Override
    public void delActionLogById(String id) {
        actionLogMapper.delActionLogById(id);
    }

    @Override
    public void delActionLogByIds(List<String> ids) {
        actionLogMapper.delActionLogByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getActionLogs(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getActionLogs(map);
        PageInfo<Map<String, Object>> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getActionLogs(Map<String, Object> map) {
        List<Map<String, Object>> actionLogs = actionLogMapper.getActionLogs(map);
        for (Map<String, Object> actionLog : actionLogs) {
            if (actionLog.get("type") != null) {
                actionLog.put("typeName", DictUtil.keyValue("LOG_TYPE", actionLog.get("type").toString()));
            }
        }
        return actionLogs;
    }

     @Override
     public void delActionLogReals() {
         actionLogMapper.delActionLogReals();
     }

     @Override
     public List<Map<String, Object>> getLogGroupByType(String type) {
         List<Map<String, Object>> list = new ArrayList<>();
        switch (type){
            case "date":
                list = actionLogMapper.getLogGroupByDate();
                break;
            case "month":
                list = actionLogMapper.getLogGroupByMonth();
                break;
            case "year":
                list = actionLogMapper.getLogGroupByYear();
                break;
        }
         return list;
     }
 }
