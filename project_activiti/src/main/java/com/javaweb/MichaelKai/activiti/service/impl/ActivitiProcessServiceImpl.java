package com.javaweb.MichaelKai.activiti.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.common.constants.ActivitiConstant;
import org.activiti.engine.history.*;
import org.activiti.image.HMProcessDiagramGenerator;
import com.javaweb.MichaelKai.activiti.service.ActivitiProcessService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.Base64Util;
import com.javaweb.MichaelKai.pojo.BaseTask;
import com.javaweb.MichaelKai.service.UserService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-08-09 10:28
 **/
@Service
@Transactional
public class ActivitiProcessServiceImpl implements ActivitiProcessService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private UserService userService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    ProcessEngineFactoryBean processEngine;


    @Override
    public void startProcess(String deploymentId, String businessKey) {
        ProcessDefinition processDefinition = getProcessDefinition(deploymentId);
        runtimeService.startProcessInstanceByKey(processDefinition.getKey(), businessKey);
    }

    @Override
    public void startProcess(String deploymentId) {
        ProcessDefinition processDefinition = getProcessDefinition(deploymentId);
        runtimeService.startProcessInstanceByKey(processDefinition.getKey());
    }

    private ProcessDefinition getProcessDefinition(String deploymentId) {
        if (StringUtils.isEmpty(deploymentId)) {
            throw new ResultException(ResultEnum.DEPLOYMENT_ID_ISNULL.getValue(), ResultEnum.DEPLOYMENT_ID_ISNULL.getMessage());
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId)
                .singleResult();
        return processDefinition;
    }

    @Override
    public List<Task> getTaskByUserId(String userId, String processDefinitionKey) {
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateOrAssigned(userId).list();
        return list;
    }

    @Override
    public PageInfo getTaskByUserId(int page, int limit, String userId, String processDefinitionKey) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId).listPage(page - 1, limit);

        List<com.javaweb.MichaelKai.activiti.pojo.Task> list = Lists.newArrayList();

        for (Task task : tasks) {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            BaseTask baseTask = (BaseTask) variables.get("baseTask");
            com.javaweb.MichaelKai.activiti.pojo.Task taskEntity = new com.javaweb.MichaelKai.activiti.pojo.Task(task);
            taskEntity.setReason(baseTask.getReason());
            taskEntity.setUrlPath(baseTask.getUrlPath());

            Map<String, Object> userById = userService.getUserById(baseTask.getUserId());
            taskEntity.setUserName(userById.get("userName").toString());

            //判断当前办理人是否是自己
            if (userId.equals(baseTask.getUserId())) {
                if (variables.containsKey("flag") && !StringUtils.isEmpty(variables.get("flag"))) {
                    //判断流程是否通过
                    if (!(boolean) variables.get("flag")) {//如果flag为false，则表示不通过,对应的前端操作不相同
                        taskEntity.setFlag(true);
                    } else {
                        taskEntity.setFlag(false);
                    }
                } else {
                    taskEntity.setFlag(true);
                }

            } else {//如果不是自己则flag为false
                taskEntity.setFlag(false);
            }

            list.add(taskEntity);
        }

        PageInfo pageList = new PageInfo(list);
        return pageList;
    }

    @Override
    public Map<String, Object> getVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    @Override
    public Task getTaskById(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task;
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    @Override
    public List<String> getUsersForSL(DelegateExecution execution) {
        return null;
    }

    @Override
    public List<String> getUsersForSP(DelegateExecution execution) {
        return null;
    }

    @Override
    public void queryProImg(String processInstanceId) throws Exception {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        //根据流程定义获取输入流
        InputStream inputStream = repositoryService.getProcessDiagram(historicProcessInstance.getProcessDefinitionId());
        BufferedImage bi = ImageIO.read(inputStream);
        File file = new File("my-process-bpmn20.png");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ImageIO.write(bi, "png", fileOutputStream);
        fileOutputStream.close();
        inputStream.close();
    }

    @Override
    public List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {

        List<String> highFlows = Lists.newArrayList();//保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
            ActivityImpl activity = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());

            List<ActivityImpl> sameStartTimeNodes = Lists.newArrayList();//保存需要开始时间相同的节点
            ActivityImpl activity1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());

            //将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(activity1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                //后续第一个节点
                HistoricActivityInstance historicActivityInstance1 = historicActivityInstances.get(j);
                //后续第二个节点
                HistoricActivityInstance historicActivityInstance2 = historicActivityInstances.get(j + 1);
                if (historicActivityInstance1.getStartTime().equals(historicActivityInstance2.getStartTime())) {
                    //如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl activity2 = processDefinitionEntity.findActivity(historicActivityInstance2.getActivityId());
                    sameStartTimeNodes.add(activity2);
                } else {
                    break;
                }
            }

            //取出节点的所有出去的线
            List<PvmTransition> pvmTransitions = activity.getOutgoingTransitions();
            for (PvmTransition pvmTransition : pvmTransitions) {
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }


        }
        return highFlows;
    }

    @Override
    public PageInfo getUnfinishedTask(int page, int limit, Map<String, Object> map) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().unfinished().listPage(page - 1, limit);
        PageInfo pageInfo = new PageInfo<>(historicTaskInstances);
        return pageInfo;
    }

    @Override
    public PageInfo getFinishedTask(int page, int limit, Map<String, Object> map) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().finished().listPage(page - 1, limit);
        PageInfo<HistoricTaskInstance> pageInfo = new PageInfo<>(historicTaskInstances);
        return pageInfo;
    }

    @Override
    public PageInfo getUserHistoryTask(int page, int limit, Map<String, Object> map) {

        //代理人办理过的任务
        List<HistoricTaskInstance> assignessHisTasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(map.get("userId").toString()).listPage(page - 1, limit);
        PageInfo<HistoricTaskInstance> pageInfo = new PageInfo<>(assignessHisTasks);
        return pageInfo;
    }

    @Override
    public Map<String, Object> getHighLightProcImage(HttpServletRequest request, HttpServletResponse resp, String processInstanceId) {

        try {
            Map<String, Object> map = Maps.newHashMap();
            JSONArray imageBase64 = new JSONArray();
            InputStream imageStream = generateImageStream(processInstanceId, true);
            if (imageStream != null) {
                String imageCurrentNode = Base64Util.ioToBase64(imageStream);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(imageCurrentNode)) {
                    imageBase64.add(imageCurrentNode);
                }
            }
            InputStream imageNoCurrentStream = generateImageStream(processInstanceId, false);
            if (imageNoCurrentStream != null) {
                String imageNoCurrentNode = Base64Util.ioToBase64(imageNoCurrentStream);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(imageNoCurrentNode)) {
                    imageBase64.add(imageNoCurrentNode);
                }
            }
            map.put("images", imageBase64);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ACT_PROCESS_IMAGE_ERROR.getValue(), ResultEnum.ACT_PROCESS_IMAGE_ERROR.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getProcessDetail(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        List<Map<String, Object>> approvalList = Lists.newArrayList();
        //判断当前流程是否运行中
        if (processInstance != null) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            Map<String, Object> variables = taskService.getVariables(task.getId());
            Object o = variables.get(ActivitiConstant.APPROVAL_MESSAGE);
            if (o != null) {
                approvalList = (List<Map<String, Object>>) o;
            }
        } else {
            List<HistoricDetail> list = historyService.createHistoricDetailQuery()
                    .processInstanceId(processInstanceId).list();
            HistoricVariableUpdate variable;
            for (HistoricDetail historicDetail : list) {
                variable = (HistoricVariableUpdate) historicDetail;
                String variableName = variable.getVariableName();
                if (ActivitiConstant.APPROVAL_MESSAGE.equals(variableName)) {
                    approvalList.clear();
                    approvalList.addAll((List<Map<String, Object>>) variable.getValue());
                }
            }
        }
        return approvalList;
    }

    /**
     * @Description 获取当前流程图，历史流程
     *
     * @Author YuKai Fan
     * @Date 19:50 2019/8/9
     * @Param isCurrent:是否获取当前流程
     * @return
     **/
    private InputStream generateImageStream(String processInstanceId, boolean isCurrent) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        String processDefinitionId = null;
        //执行过的activityId集合
        List<String> executedActivityIdList = Lists.newArrayList();
        //当前执行的activityId集合
        List<String> currentActivityIdList = Lists.newArrayList();
        //历史执行的activityId集合
        List<HistoricActivityInstance> historicActivityInstanceList = Lists.newArrayList();

        if (processInstance != null) {//判断当前流程实例是否为空
            processDefinitionId = processInstance.getProcessDefinitionId();
            if (isCurrent) {//是否获取当前流程
                currentActivityIdList = runtimeService.getActiveActivityIds(processInstance.getId());
            }
        }

        if (historicProcessInstance != null) {//判断历史流程实例是否为空
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
            historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();
            for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                executedActivityIdList.add(historicActivityInstance.getActivityId());
            }
        }

        if (StringUtils.isEmpty(processDefinitionId) || CollectionUtils.isEmpty(executedActivityIdList)) {
            return null;
        }

        //高亮线路集合id
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        List<String> highLightedFlows = getHighLightedFlows(processDefinitionEntity, historicActivityInstanceList);

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        HMProcessDiagramGenerator diagramGenerator = (HMProcessDiagramGenerator) processEngineConfiguration.getProcessDiagramGenerator();


        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png",
                executedActivityIdList, highLightedFlows, "宋体",
                "微软雅黑","黑体", null, 1.0,
                currentActivityIdList);

        return imageStream;
    }
}