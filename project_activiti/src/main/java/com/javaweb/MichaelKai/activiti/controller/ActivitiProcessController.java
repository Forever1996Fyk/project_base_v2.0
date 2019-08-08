package com.javaweb.MichaelKai.activiti.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.activiti.service.ActivitiService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.AppUtil;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserLeave;
import com.javaweb.MichaelKai.service.UserLeaveService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName ActivitiProcessController
 * @Description TODO
 * @Author YuKai Fan
 * @Date 2019/8/5 21:13
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/activiti")
public class ActivitiProcessController {

    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private UserLeaveService userLeaveService;

    /**
     * @Description 启动流程
     *
     * @Author YuKai Fan
     * @Date 21:33 2019/8/5
     * @Param
     * @return
     **/
    @PostMapping("/startProcess")
    public Result startProcess(@RequestParam String deploymentId) {
        activitiService.startProcess(deploymentId);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * @Description 获取待办任务列表
     *
     * @Author YuKai Fan
     * @Date 21:34 2019/8/5
     * @Param
     * @return
     **/
    @GetMapping("/getTasksForSL")
    public Result getTasksForSL(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                                @RequestParam Map<String, Object> map) {

        User user = ShiroKit.getUser();
        PageInfo pageList = activitiService.getTaskByUserId(page, limit,
                user.getId(), null);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * @Description 完成任务
     *
     * @Author YuKai Fan
     * @Date 21:41 2019/8/5
     * @Param taskId:任务id, id:业务id, flag: 是否通过标识
     * @return
     **/
    @PostMapping("/completeTaskSL/{taskId}/{type}/{flag}")
    public Result completeTaskForSL(@RequestParam Map<String, Object> map, @PathVariable("taskId") String taskId,
                                    @PathVariable("type") String type, @PathVariable("flag") boolean flag) throws Exception {

        switch (type) {
            case "leave":
                UserLeave userLeave = (UserLeave) MapUtil.mapToObject(UserLeave.class, map);
                userLeaveService.editUserLeaveById(userLeave);

                Map<String, Object> variable = Maps.newHashMap();
                if (flag) {
                    variable.put("flag", true);
                } else {
                    variable.put("flag", false);
                }


                break;
        }

        //受理后，任务列表数据减少
        activitiService.completeTask(map.get("taskId").toString(), map.get("userId").toString(), map.get("result").toString());
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    @PostMapping("/queryProHighLighted")
    public Result queryProHighLighted(@RequestParam String processInstanceId) throws Exception {
        String imageByteArray = activitiService.queryProHighLighted(processInstanceId);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), imageByteArray);
    }

}
