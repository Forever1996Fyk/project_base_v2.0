package com.javaweb.MichaelKai.activiti.service;

import com.github.pagehelper.PageInfo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * @program: project_base
 * @description: activiti服务
 * @author: YuKai Fan
 * @create: 2019-08-05 16:25
 **/
public interface ActivitiService {

    /**
     * 启动流程
     * @param id 流程图id
     * @param businessKey 业务key
     */
    void startProcess(String id, String businessKey);

    /**
     * 根据用户id查询待办任务列表
     * @param userId
     * @return
     */
    List<Task> getTaskByUserId(String userId, String processDefinitionKey);

    /**
     * 根据用户id, key查询待办任务列表(分页)
     * @param page
     * @param limit
     * @param userId
     * @param processDefinitionKey
     * @return
     */
    PageInfo<Task> getTaskByUserId(int page, int limit, String userId, String processDefinitionKey);

    /**
     * 根据taskId获取任务信息
     * @param taskId
     * @return
     */
    Task getTaskById(String taskId);

    /**
     * 任务审批
     * @param taskId 任务id
     * @param userId 用户id
     * @param result 结果(通过/拒绝)
     */
    void completeTask(String taskId, String userId, String result);

    /**
     * 更改业务流程状态
     * @param execution
     * @param status
     */
    void updateBizStatus(DelegateExecution execution, String status);

    /**
     * 流程节点权限用户列表
     * @param execution
     * @return
     */
    List<String> getUsersForSL(DelegateExecution execution);

    /**
     * 流程几点权限用户列表
     * @param execution
     * @return
     */
    List<String> getUsersForSP(DelegateExecution execution);

    /**
     * 生成流程图
     * @param processInstanceId
     * @throws Exception
     */
    void queryProImg(String processInstanceId) throws Exception;

    /**
     * 流程图高亮显示
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    String queryProHighLighted(String processInstanceId) throws Exception;

    /**
     * 获取需要高亮的线
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances);
}

