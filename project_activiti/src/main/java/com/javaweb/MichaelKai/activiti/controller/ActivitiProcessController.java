package com.javaweb.MichaelKai.activiti.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.activiti.service.ActivitiProcessService;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserLeave;
import com.javaweb.MichaelKai.service.UserLeaveService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private ActivitiProcessService activitiProcessService;
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
        activitiProcessService.startProcess(deploymentId);
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
        PageInfo pageList = activitiProcessService.getTaskByUserId(page, limit,
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
        activitiProcessService.completeTask(map.get("taskId").toString(), map.get("userId").toString(), map.get("result").toString());
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    @PostMapping("/queryProHighLighted")
    public Result queryProHighLighted(@RequestParam String processInstanceId) throws Exception {
        String imageByteArray = activitiProcessService.queryProHighLighted(processInstanceId);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), imageByteArray);
    }

    /**
     * 获取正在运行或者已结束的任务
     * @return
     */
    @GetMapping("/getStatusTasks")
    public Result getStatusTasks(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                 @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                                 @RequestParam Map<String, Object> map) {
        PageInfo pageList = new PageInfo<>();
        if (map.containsKey("status") && !StringUtils.isEmpty(map.get("status"))) {
            if ("running".equals(map.get("status"))) {
                pageList = activitiProcessService.getUnfinishedTask(page, limit, map);
            } else {
                pageList = activitiProcessService.getFinishedTask(page, limit, map);
            }
        }

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 获取当前用户办理的任务
     * @return
     */
    @GetMapping("/getUserHistoryTask")
    public Result getUserHistoryTask(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                 @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                                 @RequestParam Map<String, Object> map) {
        User user = ShiroKit.getUser();
        map.put("userId", user.getId());

        PageInfo pageList = activitiProcessService.getUserHistoryTask(page, limit, map);

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 获取高亮流程图
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getHighLightProcImage")
    public Result getHighLightProcImage(HttpServletRequest request, HttpServletResponse resp, @RequestParam String processInstanceId) {
        Map<String, Object> map = activitiProcessService.getHighLightProcImage(request, resp, processInstanceId);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

}
