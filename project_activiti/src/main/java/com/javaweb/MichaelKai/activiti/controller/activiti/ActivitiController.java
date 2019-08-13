package com.javaweb.MichaelKai.activiti.controller.activiti;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.activiti.service.ActivitiService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.vo.ActivitiModelVo;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private ActivitiService activitiService;

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
        PageInfo<Model> pageList = activitiService.getModels(page, limit, map);
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
        String modelId = activitiService.createNewModel(request, response, activitiModelVo);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), modelId);
    }

    /**
     * 删除模型
     * @param id
     * @return
     */
    @DeleteMapping("/delModel")
    public Result delModel(@RequestParam String id) {
        activitiService.delModel(id);
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
        PageInfo<com.javaweb.MichaelKai.activiti.pojo.ProcessDefinition> pageList = activitiService.getActProcessDeploys(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }


    /**
     * 根据id部署流程
     * @param id
     * @return
     */
    @PostMapping("/deployment")
    public Result deploy(@RequestParam String id) throws IOException {
        activitiService.deployByModelId(id);
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
        activitiService.delProcessDeployById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }


    /**
     * 同步用户,角色数据 到Activit表中
     * @return
     */
    @PostMapping("/synchronizeData")
    public Result synchronizeData() {
        activitiService.synchronizeData();
        return new Result(true, ResultEnum.SUCCESS.getValue(), "同步" + ResultEnum.SUCCESS.getMessage());
    }

}