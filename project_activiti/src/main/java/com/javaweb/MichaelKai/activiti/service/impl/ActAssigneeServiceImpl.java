package com.javaweb.MichaelKai.activiti.service.impl;

import com.google.common.collect.Lists;
import com.javaweb.MichaelKai.activiti.service.ActAssigneeService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @program: project_base
 * @description: 流程办理人
 * @author: YuKai Fan
 * @create: 2019-08-07 11:59
 **/
public class ActAssigneeServiceImpl implements ActAssigneeService {
    @Autowired
    private RepositoryService repositoryService;

    @Override
    public int deleteByNodeId(String nodeId) {
        return 0;
    }

    @Override
    public List<ActivityImpl> getActivityList(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId).singleResult();

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinition.getId());
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(new InputStreamSource(resourceAsStream), false, true);
        return selectAllActivity(processDefinitionEntity.getActivities());
    }

    @Override
    public List<ActivityImpl> selectAllActivity(List<ActivityImpl> activities) {
        List<ActivityImpl> list = Lists.newArrayList();
        for (ActivityImpl activity : activities) {
            List<ActivityImpl> childActivities = activity.getActivities();
            if (!CollectionUtils.isEmpty(childActivities)) {
                list.addAll(selectAllActivity(childActivities));
            }
        }
        return list;
    }
}