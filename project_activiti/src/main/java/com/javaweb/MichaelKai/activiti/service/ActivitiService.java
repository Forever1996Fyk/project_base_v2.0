package com.javaweb.MichaelKai.activiti.service;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.activiti.pojo.ProcessDefinition;
import com.javaweb.MichaelKai.vo.ActivitiModelVo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.task.Task;
import sun.nio.cs.ext.IBM037;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: activiti服务
 * @author: YuKai Fan
 * @create: 2019-08-05 16:25
 **/
public interface ActivitiService {

    /**
     * 获取所有模型列表
     * @param page
     * @param limit
     * @param map
     * @return
     */
    PageInfo<Model> getModels(int page, int limit, Map<String, Object> map);

    /**
     * 创建新的模型
     * @param request
     * @param response
     * @param activitiModelVo
     */
    String createNewModel(HttpServletRequest request, HttpServletResponse response, ActivitiModelVo activitiModelVo);

    /**
     * 删除模型
     * @param id
     */
    void delModel(String id);

    /**
     * 流程管理列表
     * @param page
     * @param limit
     * @param map
     * @return
     */
    PageInfo<ProcessDefinition> getActProcessDeploys(int page, int limit, Map<String, Object> map);

    /**
     * 根据id部署流程
     * @param id 模型id
     */
    void deployByModelId(String id);

    /**
     * 删除流程定义 级联 删除流程节点绑定信息
     * @param deploymentId 部署id
     */
    void delProcessDeployById(String deploymentId);

    /**
     * 同步用户,角色数据 到Activit表中
     */
    void synchronizeData();

}

