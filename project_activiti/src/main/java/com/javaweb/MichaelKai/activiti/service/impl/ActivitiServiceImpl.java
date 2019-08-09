package com.javaweb.MichaelKai.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.javaweb.MichaelKai.activiti.service.ActivitiService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.vo.ActivitiModelVo;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-08-05 16:34
 **/
@Service
@Transactional
public class ActivitiServiceImpl implements ActivitiService {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TaskService taskService;

    @Override
    public PageInfo<Model> getModels(int page, int limit, Map<String, Object> map) {
        List<Model> models = repositoryService.createModelQuery()
                .orderByCreateTime().desc()
                .listPage(page - 1, limit);
        PageInfo pageList = new PageInfo<>(models);
        return pageList;
    }

    @Override
    public String createNewModel(HttpServletRequest request, HttpServletResponse response, ActivitiModelVo activitiModelVo) {
        try {
            //初始化一个空模型
            Model model = repositoryService.newModel();

            //设置节点信息
            ObjectNode modelNode = objectMapper.createObjectNode();
            modelNode.put(ModelDataJsonConstants.MODEL_NAME, activitiModelVo.getName());
            modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, activitiModelVo.getDescription());
            modelNode.put(ModelDataJsonConstants.MODEL_REVISION, activitiModelVo.getRevision());

            model.setName(activitiModelVo.getName());
            model.setKey(activitiModelVo.getKey());
            model.setMetaInfo(modelNode.toString());

            //保存模型
            repositoryService.saveModel(model);

            //完善ModelEditorSource
            String id = model.getId();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("UTF-8"));

            return id;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ERROR.getValue(), "模型创建失败");
        }
    }

    @Override
    public void delModel(String id) {
        repositoryService.deleteModel(id);
    }

    @Override
    public PageInfo<com.javaweb.MichaelKai.activiti.pojo.ProcessDefinition> getActProcessDeploys(int page, int limit, Map<String, Object> map) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        List<ProcessDefinition> processDefinitions;
        //根据deploymentId, name查询流程
        if (map.containsKey("deploymentId") && !StringUtils.isEmpty(map.get("deploymentId"))) {
            processDefinitionQuery.deploymentId(map.get("deploymentId").toString());
        }
        if (map.containsKey("name") && !StringUtils.isEmpty(map.get("name"))) {
            processDefinitionQuery.processDefinitionNameLike("%" + map.get("name").toString() + "%");
        }
        if (map.containsKey("key") && !StringUtils.isEmpty(map.get("key"))) {
            processDefinitionQuery.processDefinitionKey(map.get("key").toString());
        }

        processDefinitions = processDefinitionQuery.listPage(page - 1, limit);
        List<com.javaweb.MichaelKai.activiti.pojo.ProcessDefinition> list = Lists.newArrayList();
        processDefinitions
                .forEach(processDefinition -> list.add(new com.javaweb.MichaelKai.activiti.pojo.ProcessDefinition(processDefinition)));
        PageInfo pageList = new PageInfo<>(list);

        return pageList;
    }

    @Override
    public void deployByModelId(String id) {
        try {
            Model model = repositoryService.getModel(id);
            byte[] bytes = repositoryService.getModelEditorSource(model.getId());

            if (bytes == null) {
                throw new ResultException(ResultEnum.MODEL_DATA_ISNULL.getValue(), ResultEnum.MODEL_DATA_ISNULL.getMessage());
            }
            JsonNode jsonNode = objectMapper.readTree(bytes);

            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
            if (bpmnModel.getProcesses().size() == 0) {
                throw new ResultException(ResultEnum.MODEL_DATA_ERROR.getValue(), ResultEnum.MODEL_DATA_ERROR.getMessage());
            }

            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            //发布流程
            String processName = model.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                    .name(model.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ERROR.getValue(), e.getMessage());
        }
    }

    @Override
    public void delProcessDeployById(String deploymentId) {
        try {
            /*List<ActivityImpl> activityList = actAssigneeService.getActivityList(id);
            for (ActivityImpl activity : activityList) {
                String nodeId = activity.getId();
                if (StringUtils.isEmpty(nodeId) || "start".equals(nodeId) || "end".equals(nodeId)) {
                    continue;
                }
                *//** 接触节点和代办关联 *//**//*
                actAssigneeService.deleteByNodeId(nodeId);*//*
            }*/
            //级联删除,会删除和当前规则相关的所有信息，包括历史
            repositoryService.deleteDeployment(deploymentId, true);

            //通知正在走这条流程的用户,流程失败,因为流程改变

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ACT_PROCESS_DEPLOY_DEL_FAIL.getValue(), ResultEnum.ACT_PROCESS_DEPLOY_DEL_FAIL.getMessage());
        }
    }

    @Override
    public void synchronizeData() {
        try {
            //同步用户
            synchronizeUser();

            //同步角色 组
            synchronizeGroup();

            //同步用户-用户组关联
            synchronizeMemberShip();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ACT_SYNCHRONIZE_DATA_FAIL.getValue(), ResultEnum.ACT_SYNCHRONIZE_DATA_FAIL.getMessage());
        }
    }

    private void synchronizeMemberShip() {
        List<Map<String, Object>> allUserRoles = userService.getAllUserRoles();

        if (!CollectionUtils.isEmpty(allUserRoles)) {
            for (Map<String, Object> allUserRole : allUserRoles) {
                identityService.deleteMembership(allUserRole.get("userId").toString(), allUserRole.get("roleId").toString());
                identityService.createMembership(allUserRole.get("userId").toString(), allUserRole.get("roleId").toString());
            }
        }
    }

    private void synchronizeGroup() {
        List<Map<String, Object>> roles = roleService.getRoles(null);
        Group group;
        if (!CollectionUtils.isEmpty(roles)) {
            for (Map<String, Object> role : roles) {
                group = new GroupEntity();
                group.setId(role.get("id").toString());
                group.setName(role.get("roleName").toString());
                identityService.deleteGroup(role.get("id").toString());
                identityService.saveGroup(group);
            }
        }
    }

    private void synchronizeUser() {
        List<Map<String, Object>> users = userService.getUsers(null);
        User au;

        if (!CollectionUtils.isEmpty(users)) {
            for (Map<String, Object> user : users) {
                au = new UserEntity();
                au.setId(user.get("id").toString());
                au.setFirstName(user.get("userName").toString());
                au.setLastName(user.get("nickName").toString());
                au.setEmail(user.get("email").toString());
                au.setPassword(user.get("password").toString());
                identityService.deleteUser(user.get("id").toString());
                identityService.saveUser(au);
            }
        }

    }
}