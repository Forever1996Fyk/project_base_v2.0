package com.javaweb.MichaelKai.activiti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.javaweb.MichaelKai.activiti.service.ActAssigneeService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.vo.ActivitiModelVo;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 工作流模型controller
 * @author: YuKai Fan
 * @create: 2019-08-05 13:49
 **/
@RestController
@RequestMapping("/api/activiti")
public class ActivitiController {

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

    /**
     * 获取所有的模型列表(分页)
     * @param page
     * @param limit
     * @param map
     * @return
     */
    @GetMapping("/getModels")
    public Result getModels(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                            @RequestParam Map<String, Object> map) {

        //获取所有的模型列表
        List<Model> models = repositoryService.createModelQuery()
                .orderByCreateTime().desc()
                .listPage(page - 1, limit);
        PageInfo pageList = new PageInfo<>(models);

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 新建模型
     * @param request
     * @param response
     * @param activitiModelVo
     * @return
     */
    @PostMapping("/createNewModel")
    public Result createNewModel(HttpServletRequest request, HttpServletResponse response, @RequestBody  ActivitiModelVo activitiModelVo) {
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

            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), id);

            //response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + id);

        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ERROR.getValue(), "模型创建失败");
        }
    }

    /**
     * 删除模型
     * @param id
     * @return
     */
    @DeleteMapping("/delModel")
    public Result delModel(@RequestParam String id) {
        repositoryService.deleteModel(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }


    /**
     * @Description 流程管理列表
     *
     * @Author YuKai Fan
     * @Date 20:48 2019/8/7
     * @Param
     * @return
     **/
    @GetMapping("/getActProcessDeploys")
    public Result getActProcessDeploys(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                                @RequestParam Map<String, Object> map) {
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

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


    /**
     * 根据id部署流程
     * @param id
     * @return
     */
    @PostMapping("/deployment")
    public Result deploy(@RequestParam String id) throws IOException {
        Model model = repositoryService.getModel(id);
        byte[] bytes = repositoryService.getModelEditorSource(model.getId());

        if (bytes == null) {
            return new Result(false, ResultEnum.MODEL_DATA_ISNULL.getValue(), ResultEnum.MODEL_DATA_ISNULL.getMessage());
        }
        JsonNode jsonNode = objectMapper.readTree(bytes);

        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        if (bpmnModel.getProcesses().size() == 0) {
            return new Result(false, ResultEnum.MODEL_DATA_ERROR.getValue(), ResultEnum.MODEL_DATA_ERROR.getMessage());
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
        return new Result(true, ResultEnum.SUCCESS.getValue(), "流程发布" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * @Description 删除流程定义 级联 删除流程节点绑定信息
     *
     * @Author YuKai Fan
     * @Date 21:34 2019/8/7
     * @Param
     * @return
     **/
    @DeleteMapping("/delProcessDeploy")
    public Result delProcessDeploy(@RequestParam String id) {
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
            repositoryService.deleteDeployment(id, true);

            //通知正在走这条流程的用户,流程失败,因为流程改变

            return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ACT_PROCESS_DEPLOY_DEL_FAIL.getValue(), ResultEnum.ACT_PROCESS_DEPLOY_DEL_FAIL.getMessage());
        }

    }

    /**
     * 同步用户,角色数据 到Activit表中
     * @return
     */
    @PostMapping("/synchronizeData")
    public Result synchronizeData() {

        try {
            //同步用户
            synchronizeUser();

            //同步角色 组
            synchronizeGroup();

            //同步用户-用户组关联
            synchronizeMemberShip();

            return new Result(true, ResultEnum.SUCCESS.getValue(), "同步" + ResultEnum.SUCCESS.getMessage());
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