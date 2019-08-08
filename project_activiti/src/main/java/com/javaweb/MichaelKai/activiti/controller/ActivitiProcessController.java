package com.javaweb.MichaelKai.activiti.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.javaweb.MichaelKai.activiti.service.ActivitiService;
import com.javaweb.MichaelKai.common.constants.ActivitiConstant;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.exception.ResultException;
import com.javaweb.MichaelKai.common.utils.MapUtil;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.pojo.UserLeave;
import com.javaweb.MichaelKai.service.UserLeaveService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @Description 更新任务
     *
     * @Author YuKai Fan
     * @Date 21:41 2019/8/5
     * @Param taskId:任务id, id:业务id, flag: 是否重新提交流程
     * @return
     **/
    @PostMapping("/updateTaskSL/{taskId}/{type}/{flag}")
    public Result updateTaskSL(@RequestBody Map<String, Object> map, @PathVariable("taskId") String taskId,
                                    @PathVariable("type") String type, @PathVariable("flag") boolean flag) throws Exception {

        try {
            Map<String, Object> variable = Maps.newHashMap();
            switch (type) {
                case "leave":
                    UserLeave userLeave = (UserLeave) MapUtil.mapToObject(UserLeave.class, map);
                    userLeaveService.editUserLeaveById(userLeave);
                    if (flag) {
                        variable.put("flag", true);
                    } else {
                        variable.put("flag", false);
                    }
                    break;
            }

            //受理后，任务列表数据减少
            activitiService.completeTask(taskId, variable);
            return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultException(ResultEnum.ACT_PROCESS__RETURN_SUBMIT_ERROR.getValue(), ResultEnum.ACT_PROCESS__RETURN_SUBMIT_ERROR.getMessage());
        }
    }

    /**
     * @Description 完成任务
     *
     * @Author YuKai Fan
     * @Date 21:43 2019/8/8
     * @Param flag:通过 不通过
     * @return
     **/
    @PostMapping("/completeTaskSL/{taskId}/{flag}")
    public Result completeTaskSL(@RequestBody Map<String, Object> form, @PathVariable("taskId") String taskId,
                                 @PathVariable("flag") boolean flag) {
        Map<String, Object> variables = activitiService.getVariables(taskId);
        User user = ShiroKit.getUser();

        form.put("createTime", new Date());
        form.put("opinionUserId", user.getId());//申请意见的用户id
        form.put("opinionUserName", user.getUserName());//申请意见的用户姓名

        Map<String, Object> map = Maps.newHashMap();
        map.put("flag", flag);//传入变量flag,判断是否通过

        //判断节点是否已经拒绝过一次
        Object needEnd = variables.get("needEnd");
        if (needEnd != null && (boolean) needEnd && !flag) {//如果上次被拒绝，这次还是拒绝，就是直接结束流程
            map.put("needFinish", -1);
        } else {
            if (flag) {
                map.put("needFinish", 1);//通过下一节点
            } else {
                map.put("needFinish", 0);//不通过
            }
        }

        //审批信息叠加
        List<Map<String, Object>> approvalMsgList = Lists.newArrayList();
        Object o = variables.get(ActivitiConstant.APPROVAL_MESSAGE);
        if (o != null) {
            approvalMsgList = (List<Map<String, Object>>) o;
        }

        approvalMsgList.add(form);
        map.put(ActivitiConstant.APPROVAL_MESSAGE, approvalMsgList);
        activitiService.completeTask(taskId, map);

        return new Result(true, flag?ResultEnum.APPROVED.getValue():ResultEnum.NOT_APPROVED.getValue(),
                flag?ResultEnum.APPROVED.getMessage():ResultEnum.NOT_APPROVED.getMessage());

    }

    @PostMapping("/queryProHighLighted")
    public Result queryProHighLighted(@RequestParam String processInstanceId) throws Exception {
        String imageByteArray = activitiService.queryProHighLighted(processInstanceId);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), imageByteArray);
    }

}
