package com.javaweb.MichaelKai.activiti.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.activiti.service.ActivitiProcessService;
import com.javaweb.MichaelKai.common.constants.ActivitiConstant;
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
import java.util.Date;
import java.util.List;
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
     * @Description 重新提交更新任务
     *
     * @Author YuKai Fan
     * @Date 21:41 2019/8/5
     * @Param taskId:任务id, id:业务id, flag: 是否通过标识
     * @return
     **/
    @PostMapping("/updateTaskSL/{taskId}/{type}/{flag}")
    public Result updateTaskSL(@RequestBody Map<String, Object> map, @PathVariable("taskId") String taskId,
                                    @PathVariable("type") String type, @PathVariable("flag") boolean flag) throws Exception {

        Map<String, Object> variable = Maps.newHashMap();
        switch (type) {
            case "leave":
                UserLeave userLeave = MapUtil.mapToObject(UserLeave.class, map, false);
                userLeaveService.editUserLeaveById(userLeave);

                if (flag) {
                    variable.put("flag", true);
                } else {
                    variable.put("flag", false);
                }
                break;
        }

        //受理后，任务列表数据减少
        activitiProcessService.completeTask(taskId, variable);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * @Description 完成任务
     *
     * @Author YuKai Fan
     * @Date 21:41 2019/8/5
     * @Param taskId:任务id, id:业务id, flag: 是否通过标识
     * @return
     **/
    @PostMapping("/completeTaskSL/{taskId}/{flag}")
    public Result completeTaskSL(@PathVariable("taskId") String taskId,
                                 @PathVariable("flag") boolean flag,
                                 @RequestBody Map<String, Object> map) throws Exception {
        Map<String, Object> variables = activitiProcessService.getVariables(taskId);

        User user = ShiroKit.getUser();

        //处理当前节点的信息
        map.put("createTime", new Date());
        map.put("userId", user.getId());
        map.put("userName", user.getUserName());
        map.put("flag", flag);

        Map<String, Object> form = Maps.newHashMap();
        form.put("flag", flag);

        //判断是否已经拒绝过一次
        Object needEnd = variables.get("needEnd");
        if (needEnd != null && (boolean) needEnd && (!flag)) {//如果之前已经拒绝过,再次申请，直接结束流程
            form.put("needFinish", -1);
        } else {
            if (flag) {
                form.put("needFinish", 1);//通过下一节点
            } else {
                form.put("needFinish", 0);//不通过
            }
        }

        List<Map<String, Object>> approvalMsgList = Lists.newArrayList();
        Object o = variables.get(ActivitiConstant.APPROVAL_MESSAGE);
        if (o != null) {
            approvalMsgList = (List<Map<String, Object>>) o;
        }
        approvalMsgList.add(map);

        form.put("approvalMsgList", approvalMsgList);

        //受理后，任务列表数据减少
        activitiProcessService.completeTask(taskId, form);
        return new Result(true, flag?ResultEnum.APPROVED.getValue():ResultEnum.NOT_APPROVED.getValue(),
                flag?ResultEnum.APPROVED.getMessage():ResultEnum.NOT_APPROVED.getMessage());
    }

    @GetMapping("/getHighLightProcImage")
    public Result getHighLightProcImage(HttpServletRequest request, HttpServletResponse response, @RequestParam String processInstanceId) {
        Map<String, Object> highLightProcImage = activitiProcessService.getHighLightProcImage(request, response, processInstanceId);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), highLightProcImage);
    }

    /**
     * @Description 已完成的任务
     *
     * @Author YuKai Fan
     * @Date 0:47 2019/8/10
     * @Param
     * @return
     **/
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
     * @Description 运行中的任务，已结束的任务
     *
     * @Author YuKai Fan
     * @Date 0:47 2019/8/10
     * @Param
     * @return
     **/
    @GetMapping("/getStatusTasks")
    public Result getStatusTasks(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                     @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                                     @RequestParam Map<String, Object> map) {
        PageInfo pageList = new PageInfo();
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
     * 根据流程实例id获取审批信息
     * @param processInstanceId
     * @return
     */
    @GetMapping("/getProcessDetail")
    public Result getProcessDetail(@RequestParam String processInstanceId) {
        List<Map<String, Object>> list = activitiProcessService.getProcessDetail(processInstanceId);

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

}
