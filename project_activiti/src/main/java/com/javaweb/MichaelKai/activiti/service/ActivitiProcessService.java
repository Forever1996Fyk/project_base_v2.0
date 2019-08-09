package com.javaweb.MichaelKai.activiti.service;

import com.github.pagehelper.PageInfo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-08-09 10:28
 **/
public interface ActivitiProcessService {
    /**
     * 启动流程
     * @param id 流程图id
     * @param businessKey 业务key
     */
    void startProcess(String id, String businessKey);

    /**
     * 启动流程
     * @param deploymentId 流程部署id
     */
    void startProcess(String deploymentId);

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
    PageInfo getTaskByUserId(int page, int limit, String userId, String processDefinitionKey);

    /**
     * 根据taskId.获取流程变量
     * @param taskId
     * @return
     */
    Map<String, Object> getVariables(String taskId);

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

    /**
     * 获取未结束的任务
     * @param page
     * @param limit
     * @param map
     * @return
     */
    PageInfo getUnfinishedTask(int page, int limit, Map<String, Object> map);

    /**
     * 获取已结束的任务
     * @param page
     * @param limit
     * @param map
     * @return
     */
    PageInfo getFinishedTask(int page, int limit, Map<String, Object> map);

    /**
     * 获取用户已办理的任务
     * @param page
     * @param limit
     * @param map
     * @return
     */
    PageInfo getUserHistoryTask(int page, int limit, Map<String, Object> map);
}