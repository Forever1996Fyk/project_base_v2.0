package com.javaweb.MichaelKai.activiti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.vo.ActivitiModelVo;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ObjectMapper objectMapper;

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
     * 根据id启动流程
     * @param id
     * @return
     */
    @PostMapping("/startProcess")
    public Result startProcess(@RequestParam String id) {
        try {

            return new Result(true, ResultEnum.SUCCESS.getValue(), "启动" + ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            throw new ResultException(ResultEnum.START_FAIL.getValue(), ResultEnum.START_FAIL.getMessage());
        }

    }
}