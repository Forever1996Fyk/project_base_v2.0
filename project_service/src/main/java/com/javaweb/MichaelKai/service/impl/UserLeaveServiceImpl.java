package com.javaweb.MichaelKai.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javaweb.MichaelKai.service.UserLeaveService;
import com.javaweb.MichaelKai.mapper.UserLeaveMapper;
import com.javaweb.MichaelKai.pojo.UserLeave;
import com.javaweb.MichaelKai.common.enums.StatusEnum;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


/**
 * @program: project_base
 * @description: 请假流程表
 * @author: YuKai Fan
 * @create: 2019-08-08 10:30:23
 **/
@Service
@Transactional
@SuppressWarnings("SpringJavaAutowiringInspection")
public class UserLeaveServiceImpl implements UserLeaveService {

    @Autowired
    private UserLeaveMapper userLeaveMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Override
    public UserLeave addUserLeave(UserLeave userLeave) {
        userLeave.setId(AppUtil.randomId());
        userLeave.setStatus(StatusEnum.Normal.getValue());
        userLeave.setUrlPath("/leave/readOnlyLeave/" + userLeave.getId());

        //根据processDefinitionKey启动流程实例
        Map<String, Object> map = Maps.newHashMap();
        map.put("leaveTask", userLeave);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process", map);

        userLeave.setProcessInstanceId(processInstance.getId());

        userLeaveMapper.addUserLeave(userLeave);
        return userLeave;
    }

    @Override
    public Map<String, Object> getUserLeaveById(String id) {
        return userLeaveMapper.getUserLeaveById(id);
    }

    @Override
    public void editUserLeaveById(UserLeave userLeave) {
        userLeaveMapper.editUserLeaveById(userLeave);
    }

    @Override
    public void editUserLeaveByIds(UserLeave userLeave, List<String> ids) {

    }

    @Override
    public void delUserLeaveById(String id) {
        userLeaveMapper.delUserLeaveById(id);
    }

    @Override
    public void delUserLeaveByIds(List<String> ids) {
        userLeaveMapper.delUserLeaveByIds(ids);
    }

    @Override
    public PageInfo<Map<String, Object>> getUserLeaves(int pageNum, int pageSize, Map<String, Object> map) {
        PageHelper.startPage(pageNum, pageSize);
        List<Map<String, Object>> list = this.getUserLeaves(map);
        PageInfo<Map<String, Object>> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> getUserLeaves(Map<String, Object> map) {
        List<Map<String, Object>> userLeaves = userLeaveMapper.getUserLeaves(map);
        for (Map<String, Object> userLeave : userLeaves) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(userLeave.get("processInstanceId").toString()).singleResult();

            //判断是否运行中
            if (processInstance != null) {
                Task task = taskService.createTaskQuery()
                        .processInstanceId(userLeave.get("processInstanceId").toString()).singleResult();

                //当前任务的审批阶段
                userLeave.put("taskName", task.getName());
            }
        }
        return userLeaves;
    }
}
