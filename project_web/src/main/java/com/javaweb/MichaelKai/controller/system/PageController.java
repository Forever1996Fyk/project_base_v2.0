package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.activiti.service.ProcessClassService;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.utils.HttpServletUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.pojo.BaseTask;
import com.javaweb.MichaelKai.pojo.Notice;
import com.javaweb.MichaelKai.pojo.NoticeUser;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.quartz.service.ScheduleJobService;
import com.javaweb.MichaelKai.service.*;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import org.activiti.engine.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 页面跳转controller
 * @author: YuKai Fan
 * @create: 2019-05-21 15:11
 **/
@Controller
@RequestMapping("/system")
public class PageController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SysGeneratorService sysGeneratorService;
    @Autowired
    private DictService dictService;
    @Autowired
    private DictItemService dictItemService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserLeaveService userLeaveService;
    @Autowired
    private ProcessClassService processClassService;

    /**
     * 跳转到注册页面
     * @return
     */
    @GetMapping("/toReg")
    public String toReg() {
        return "reg";
    }

    /**
     * 跳转到忘记密码页面
     * @return
     */
    @GetMapping("/toForget")
    public String toForget(Model model) {
        model.addAttribute("type", "forget");
        return "forget";
    }

    /**
     * 跳转到忘记密码页面
     * @return
     */
    @GetMapping("/toResetpass")
    public String toResetpass(String phone, Model model) {
        model.addAttribute("type", "resetpass");
        model.addAttribute("phone", phone);
        return "forget";
    }

    /**
     * 用户信息页面
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(Model model) {
        User user = ShiroKit.getUser();
        model.addAttribute("user", user);
        return "system/main/userInfo";
    }

    /**
     * 用户信息页面
     * @return
     */
    @GetMapping("/cropperPic")
    public String cropperPic() {
        return "system/main/cropperPic";
    }


    /**
     * 用户管理列表
     * @return
     */
    @RequestMapping("/user/userList")
    @RequiresPermissions("*:*:*")
    public String userList() {
        return "system/user/user";
    }

    /**
     * 用户添加页面
     * @return
     */
    @RequestMapping("/user/add")
    public String addUser() {
        return "system/user/add";
    }

    /**
     * 用户修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/user/edit")
    public String editUser(String id, Model model) {
        Map<String, Object> userById = userService.getUserById(id);
        model.addAttribute("user", userById);
        return "system/user/add";
    }

    @RequestMapping("/user/roleAssign/{ids}")
    public String roleAssign(@PathVariable("ids") String[] ids, Model model) {
        //获取所有的角色
        List<Map<String, Object>> roles = roleService.getRoles(null);

        //根据用户id获取该用户角色
        if (ids.length == 1) {
            List<Map<String, Object>> allRolesByUserId = userService.getAllRolesByUserId(ids[0]);
            if (roles.size() > 0) {
                for (Map<String, Object> roleMap : roles) {
                    if (allRolesByUserId.contains(roleMap)) {
                        roleMap.put("selected", 1);
                    } else {
                        roleMap.put("selected", 0);
                    }
                }

                Map<String, Object> roleList = new HashMap<>();
                roleList.put("userId", ids[0]);
                roleList.put("roleList", roles);

                model.addAttribute("roleList", roleList);
            }
        }

        return "system/user/roleAssign";
    }

    /**
     * 角色列表
     * @return
     */
    @RequestMapping("/role/roleList")
    public String roleList() {
        return "system/role/role";
    }

    /**
     * 角色添加
     * @return
     */
    @RequestMapping("/role/add")
    public String addRole() {
        return "system/role/add";
    }

    /**
     * 角色修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/role/edit")
    public String editRole(String id, Model model) {
        Map<String, Object> roleById = roleService.getRoleById(id);
        model.addAttribute("role", roleById);
        return "system/role/add";
    }

    /**
     * 分配权限
     * @param ids
     * @param model
     * @return
     */
    @RequestMapping("/role/auth/{ids}")
    public String roleAuth(@PathVariable("ids") String[] ids, Model model) {
        if (ids.length == 1) {
            model.addAttribute("id", ids[0]);
        }
        return "system/role/auth";
    }

    /**
     * 权限列表
     * @return
     */
    @RequestMapping("/permission/permissionList")
    public String permissionList(Model model) {
        String search = HttpServletUtil.getRequest().getQueryString();
        model.addAttribute("search", search);
        return "system/permission/permission";
    }

    /**
     * 权限添加
     * @return
     */
    @GetMapping({"/permission/add", "/permission/add/{pid}"})
    public String addPermission(@PathVariable(value = "pid", required = false) String pid, Model model) {
        if (pid != null) {
            Map<String, Object> permission = permissionService.getPermissionById(pid);
        }
        return "system/permission/add";
    }

    /**
     * 权限修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/permission/edit/{id}")
    public String editPermission(@PathVariable(value = "id") String id, Model model) {
        Map<String, Object> permission = permissionService.getPermissionById(id);
        Map<String, Object> pPermission = permissionService.getPermissionById(permission.get("pid").toString());
        model.addAttribute("permission", permission);
        model.addAttribute("pPermission", pPermission);
        return "system/permission/add";
    }

    /**
     * 代码生成页面(数据库表，字段列表)
     * @return
     */
    @RequestMapping("/generator/code")
    public String generatorTableColumn() {
        return "system/generator/tableColumn";
    }

    /**
     * 代码生成页面(数据库表，字段列表)
     * @return
     */
    @RequestMapping("/generator/columnsView/{tableName}")
    public String columnsView(@PathVariable(value = "tableName") String tableName, Model model) {
        List<Map<String, String>> list = sysGeneratorService.queryColumns(tableName);
        model.addAttribute("list", list);
        model.addAttribute("tableName", tableName);
        return "system/generator/columnsView";
    }

    /**
     * 数据字典管理列表
     * @return
     */
    @RequestMapping("/basicDict/dictList")
    public String dictList() {
        return "system/basicDict/dict";
    }

    /**
     * 数据字典添加页面
     * @return
     */
    @RequestMapping("/basicDict/addDict")
    public String addDict() {
        return "system/basicDict/addDict";
    }

    /**
     * 数据字典修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/basicDict/editDict")
    public String editDict(String id, Model model) {
        Map<String, Object> dictById = dictService.getDictById(id);
        model.addAttribute("dict", dictById);
        return "system/basicDict/addDict";
    }

    /**
     * 根据dic_id获取数据字典项
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/basicDict/dictItemsView")
    public String dictItemsView(String id, Model model) {
        Map<String, Object> dictById = dictService.getDictById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("dicId", id);
        List<Map<String, Object>> dictItems = dictItemService.getDictItems(map);

        model.addAttribute("dict", dictById);
        model.addAttribute("list", dictItems);
        return "system/basicDict/dictItem";
    }

    /**
     * 数据字典添加页面
     * @return
     */
    @RequestMapping("/basicDict/addDictItem/{dicId}")
    public String addDictItem(@PathVariable(value = "dicId") String dicId, Model model) {
        model.addAttribute("dicId", dicId);
        return "system/basicDict/addDictItem";
    }

    /**
     * 数据字典修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/basicDict/editDictItem/{id}")
    public String editDictItem(@PathVariable("id") String id, Model model) {
        Map<String, Object> dictItemById = dictItemService.getDictItemById(id);
        model.addAttribute("dictItem", dictItemById);
        return "system/basicDict/addDictItem";
    }

    /**
     * 用户操作日志列表
     * @return
     */
    @GetMapping("/actionLog/actionLogList")
    public String actionLogList() {
        return "system/actionLog/actionLog";
    }

    /**
     * 主页控制台
     * @return
     */
    @GetMapping("/main/console")
    public String consoleMain(Model model) {
        User user = ShiroKit.getUser();
        model.addAttribute("user", user);
        return "system/main/console";
    }

    /**
     * 通知通告管理
     * @return
     */
    @GetMapping("/notice/noticeList")
    public String noticeList() {
        return "system/notice/notice";
    }

    /**
     * 添加新的通告
     * @return
     */
    @GetMapping("/notice/add")
    public String addNotice() {
        return "system/notice/addNotice";
    }

    /**
     * 修改通告
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/notice/edit")
    public String editNotice(String id, Model model) {
        Map<String, Object> noticeById = noticeService.getNoticeById(id);
        model.addAttribute("notice", noticeById);
        return "system/notice/addNotice";
    }

    /**
     * 选择通知对象
     * @return
     */
    @GetMapping("/notice/userNotice/{ids}")
    public String userNotice(@PathVariable("ids") String[] ids, Model model) {
        if (ids.length == 1) {
            model.addAttribute("id", ids[0]);
            model.addAttribute("userId", ShiroKit.getUser().getId());
        }
        return "system/notice/userNotice";
    }

    /**
     * 我的通告
     * @return
     */
    @GetMapping("/notice/myNotice")
    public String myNotice() {
        return "system/notice/myNotice";
    }

    /**
     * 查看通告内容
     * @return
     */
    @GetMapping("/notice/contentView")
    public String contentView(Integer id, String noticeId, Model model) {
        Map<String, Object> noticeById = noticeService.getNoticeById(noticeId);
        model.addAttribute("notice", noticeById);

        //更新通告为已读
        NoticeUser noticeUser = new NoticeUser();
        noticeUser.setId(id);
        noticeUser.setReaded(1);
        noticeUser.setReadedTime(DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_COMMON));
        noticeService.editNoticeUserById(noticeUser);

        return "system/notice/contentView";
    }

    @GetMapping("/layIm/index")
    public String ImIndex() {
        return "system/im/index";
    }

    /**
     * 个人网盘
     * @return
     */
    @GetMapping("/cloudDisk/disk")
    public String cloudDisk(Model model) {
        model.addAttribute("diskFlag", "disk");
        return "system/cloudDisk/disk";
    }

    /**
     * 公共网盘
     * @return
     */
    @GetMapping("/cloudDisk/commonDisk")
    public String commonDisk(Model model) {
        model.addAttribute("diskFlag", "commonDisk");
        return "system/cloudDisk/disk";
    }

    /**
     * quartz任务管理
     * @return
     */
    @GetMapping("/quartz/scheduleJob")
    public String scheduleJob() {
        return "system/quartz/scheduleJob";
    }

    /**
     * 新建任务
     * @return
     */
    @GetMapping("/quartz/scheduleJob/add")
    public String addScheduleJob() {
        return "system/quartz/addScheduleJob";
    }

    /**
     * 编辑任务
     * @return
     */
    @GetMapping("/quartz/scheduleJob/edit")
    public String editScheduleJob(Model model, String id) {
        Map<String, Object> scheduleJob = scheduleJobService.getScheduleJobById(id);
        model.addAttribute("scheduleJob", scheduleJob);
        return "system/quartz/addScheduleJob";
    }

    /**
     * 工作流 模型管理列表
     * @return
     */
    @GetMapping("/activiti/modelList")
    public String activitiModelList() {
        return "system/workflow/model/modelList";
    }

    /**
     * 工作流 创建模型
     * @return
     */
    @GetMapping("/activiti/createModel")
    public String createModel() {
        return "system/workflow/model/createModel";
    }

    /**
     * 工作流 流程管理列表
     * @return
     */
    @GetMapping("/activiti/processList")
    public String activitiProcessList() {
        return "system/workflow/process/processList";
    }


    /**
     * 工作流 流程申请列表
     * @return
     */
    @GetMapping("/activiti/processApplyList")
    public String activitiProcessApply(Model model) {
        List<Map<String, Object>> processClassList = processClassService.getProcessClasss(null);
        model.addAttribute("processClassList", processClassList);
        return "system/workflow/process/processApplyList";
    }

    /**
     * 我的申请列表
     * @return
     */
    @GetMapping("/activiti/myProcessApplyList")
    public String myProcessApply(Model model) {
        List<Map<String, Object>> processClassList = processClassService.getProcessClasss(null);
        model.addAttribute("processClassList", processClassList);
        return "system/workflow/process/myProcessApplyList";
    }

    /**
     * 请假流程申请
     * @return
     */
    @GetMapping("/activiti/leaveApply")
    public String leaveApply() {
        return "system/workflow/leave/leaveApply";
    }

    /**
     * 请假流程申请列表
     * @return
     */
    @GetMapping("/activiti/leaveApplyList")
    public String leaveApplyList() {
        return "system/workflow/leave/userLeave";
    }

    /**
     * 待办任务列表
     * @return
     */
    @GetMapping("/activiti/needDealTaskList")
    public String needDealTaskList() {
        return "system/workflow/task/needDealTaskList";
    }


    /**
     * 待办任务编辑
     * @return
     */
    @GetMapping("/activiti/needDealTaskEdit/{taskId}")
    public String needDealTaskEdit(@PathVariable("taskId") String taskId, Model model) {
        Map<String, Object> variables = taskService.getVariables(taskId);
        BaseTask baseTask = (BaseTask) variables.get("leaveTask");
        Map<String, Object> userLeaveById = userLeaveService.getUserLeaveById(baseTask.getId());
        model.addAttribute("leave", userLeaveById);
        model.addAttribute("taskId", taskId);
        return "system/workflow/leave/needDealLeaveTask";
    }

    /**
     * 待办任务办理
     * @return
     */
    @GetMapping("/activiti/needDealTaskHandle")
    public String needDealTaskHandle() {
        return "system/workflow/task/needDealTaskHandle";
    }

    /**
     * 待办任务详情
     * @return
     */
    @GetMapping("/activiti/needDealTaskDetail")
    public String needDealTaskDetail() {
        return "system/workflow/task/needDealTaskDetail";
    }

    /**
<<<<<<< HEAD
     * @Description 办理任务
     *
     * @Author YuKai Fan
     * @Date 21:15 2019/8/8
     * @Param
     * @return
     **/
    @GetMapping("/activiti/handleTask/{id}")
    public String handleTask(@PathVariable("id") String taskId, Model model) {
        Map<String, Object> variables = taskService.getVariables(taskId);
        BaseTask baseTask = (BaseTask) variables.get("leaveTask");

        model.addAttribute("urlPath", baseTask.getUrlPath());
        model.addAttribute("taskId", taskId);
        return "system/workflow/task/handleTask";
    }

    /**
     * @Description 请假申请信息 只读
     *
     * @Author YuKai Fan
     * @Date 21:22 2019/8/8
     * @Param
     * @return
     **/
    @GetMapping("/leave/readOnlyLeave/{leaveId}")
    public String readOnlyLeave(@PathVariable("leaveId") String leaveId, Model model) {
        Map<String, Object> userLeaveById = userLeaveService.getUserLeaveById(leaveId);

        model.addAttribute("leave", userLeaveById);
        return "/system/workflow/leave/readOnlyLeave";
    }
    /**
     * 已办任务列表
     * @return
     */
    @GetMapping("/activiti/alreadyTaskList")
    public String alreadyTaskList() {
        return "system/workflow/task/alreadyTaskList";
    }

    /**
     * 运行中的任务列表
     * @return
     */
    @GetMapping("/activiti/runningTaskList")
    public String runningTaskList() {
        return "system/workflow/task/runningTaskList";
    }

    /**
     * 已结束的任务列表
     * @return
     */
    @GetMapping("/activiti/finishTaskList")
    public String finishTaskList() {
        return "system/workflow/task/finishTaskList";
    }

    /**
     * 查看流程图
     * @return
     */
    @GetMapping("/activiti/viewProcImage/{processInstanceId}")
    public String viewProcImage(@PathVariable("processInstanceId") String processInstanceId, Model model) {

        model.addAttribute("processInstanceId", processInstanceId);
        return "system/workflow/process/viewProcImage";
    }

    /**
     * 查看流程详情
     * @return
     */
    @GetMapping("/activiti/viewProcDetail/{processInstanceId}")
    public String viewProcDetail(@PathVariable("processInstanceId") String processInstanceId, Model model) {
        model.addAttribute("processInstanceId", processInstanceId);
        return "system/workflow/process/viewProcDetail";
    }

    /**
     * 流程分类管理列表
     * @return
     */
    @GetMapping("/activiti/processClass")
    public String processClassList() {
        return "system/workflow/processClass/processClass";
    }

    /**
     * 添加流程分类
     * @return
     */
    @GetMapping("/activiti/addProcessClass")
    public String addProcessClass() {
        return "system/workflow/processClass/addProcessClass";
    }

    /**
     * 添加流程分类
     * @return
     */
    @GetMapping("/activiti/editProcessClass")
    public String editProcessClass(Model model, String id) {
        Map<String, Object> processClassById = processClassService.getProcessClassById(id);
        model.addAttribute("processClass", processClassById);
        return "system/workflow/processClass/addProcessClass";
    }
}