package com.javaweb.MichaelKai.activiti.controller;

import com.javaweb.MichaelKai.activiti.service.customize.entity.CustomUserEntityManager;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 工作流 用户controller
 * @author: YuKai Fan
 * @create: 2019-08-06 15:40
 **/
@RestController
@RequestMapping("/api/activiti")
public class ActivitiUserController {

    @Autowired
    private CustomUserEntityManager customUserEntityManager;

    @GetMapping("/getAssigneeOrUsers")
    public Result getAssigneeOrUsers(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                     @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                                     @RequestParam Map<String, Object> map) {

        List<User> userByQueryCriteria = customUserEntityManager.findUserByQueryCriteria();
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }
}