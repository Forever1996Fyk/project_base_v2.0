package com.javaweb.MichaelKai.activiti.service;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**
 * @program: project_base
 * @description: 流程办理人service
 * @author: YuKai Fan
 * @create: 2019-08-07 11:56
 **/
public interface ActAssigneeService {

    /**
     * 根据节点id删除
     * @param nodeId
     * @return
     */
    int deleteByNodeId(String nodeId);

    /**
     * 根据部署id获取 流程
     * @param deploymentId
     * @return
     */
    List<ActivityImpl> getActivityList(String deploymentId);

    /**
     * 获取流程，以及子流程
     * @param activities
     * @return
     */
    List<ActivityImpl> selectAllActivity(List<ActivityImpl> activities);
}