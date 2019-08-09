package com.javaweb.MichaelKai.activiti.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.activiti.service.ActivitiProcessService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.pojo.BaseTask;
import com.javaweb.MichaelKai.service.UserService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    private UserService userService;
    @Autowired
    private HistoryService historyService;


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
            BaseTask userLeave = (BaseTask) variables.get("leaveTask");
            com.javaweb.MichaelKai.activiti.pojo.Task taskEntity = new com.javaweb.MichaelKai.activiti.pojo.Task(task);
            taskEntity.setReason(userLeave.getReason());
            taskEntity.setUrlPath(userLeave.getUrlPath());

            Map<String, Object> userById = userService.getUserById(userLeave.getUserId());
            taskEntity.setUserName(userById.get("userName").toString());

            //判断当前办理人是否是自己
            if (userId.equals(userLeave.getUserId())) {
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
    public void completeTask(String taskId, String userId, String result) {
        //使用这种方式，如果代办人已经有其他权限时，会抛出异常
        try {
            taskService.claim(taskId, userId);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.TASK_COMPLETE_FAIL.getValue(), ResultEnum.TASK_COMPLETE_FAIL.getMessage());
        }

        //获取任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        //获取流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        //业务处理

        //完成任务
        taskService.complete(taskId);
    }

    @Override
    public void updateBizStatus(DelegateExecution execution, String status) {
        String processBusinessKey = execution.getProcessBusinessKey();
        //根据key处理业务状态

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
    public String queryProHighLighted(String processInstanceId) throws Exception {
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(historicProcessInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> highLightedActivitiList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        //高亮环节id集合
        List<String> highLightedActivitis = Lists.newArrayList();

        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitiList);


        for (HistoricActivityInstance historicActivityInstance : highLightedActivitiList) {
            String activityId = historicActivityInstance.getActivityId();
            highLightedActivitis.add(activityId);
        }

        //配置字体
        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows,
                "宋体", "微软雅黑", "黑体", null, 2.0);
        BufferedImage bi = ImageIO.read(inputStream);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", bos);
        byte[] bytes = bos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes);//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        bos.close();
        inputStream.close();
        return png_base64;

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
            String imageBase64 = queryProHighLighted(processInstanceId);
            Map<String, Object> map = Maps.newHashMap();
            map.put("images", imageBase64);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ACT_PROCESS_IMAGE_ERROR.getValue(), ResultEnum.ACT_PROCESS_IMAGE_ERROR.getMessage());
        }
    }
}