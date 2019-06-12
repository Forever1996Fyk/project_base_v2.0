package com.javaweb.MichaelKai.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.pojo.Notice;
import com.javaweb.MichaelKai.pojo.NoticeUser;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.NoticeService;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.service.UserService;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import com.javaweb.MichaelKai.vo.NoticeVo;
import com.javaweb.michaelKai.nettySocketIO.ClientCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.vo.PageResult;

import java.util.*;


/**
 * @program: project_base
 * @description: 通知通告表
 * @author: YuKai Fan
 * @create: 2019-06-10 09:56:24
 *
 */
@RestController
@RequestMapping("/api")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClientCache clientCache;

    /**
     * 添加
     * @param notice
     * @return
     */
    @PostMapping("/notice")
    public Result addNotice(@RequestBody Notice notice) {
        return new Result(true, ResultEnum.SUCCESS.getValue(), "新增" + ResultEnum.SUCCESS.getMessage(), noticeService.addNotice(notice));
    }

    /**
     * 编辑修改
     * @param notice
     * @return
     */
    @PutMapping("/notice")
    public Result editNoticeById(@RequestBody Notice notice) {
        noticeService.editNoticeById(notice);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "修改" + ResultEnum.SUCCESS.getMessage(), notice);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping("/notice")
    public Result delNoticeById(@RequestParam String id) {
        noticeService.delNoticeById(id);
        return new Result(true, ResultEnum.SUCCESS.getValue(), "删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/notices/{ids}")
    public Result delNoticeByIds(@PathVariable("ids") String[] ids) {
        noticeService.delNoticeByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), "批量删除" + ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有(不分页)
     * @param map 参数
     * @return
     */
    @GetMapping("/getNotices/noPage")
    public Result getNotices(@RequestParam Map<String, Object> map) {
        List<Map<String, Object>> list = noticeService.getNotices(map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 获取所有
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getNotices")
    public Result getNotices(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                           @RequestParam Map<String, Object> map) {
        //获取当前登录用户
        User user = ShiroKit.getUser();
        map.put("curUserId", user.getId());
        PageInfo<Map<String, Object>> pageList = noticeService.getNotices(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 获取我的通知
     * @param page 开始记录
     * @param limit 分页大小
     * @param map 参数
     * @return
     */
    @GetMapping("/getMyNotices")
    public Result getMyNotices(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                             @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
                             @RequestParam Map<String, Object> map) {
        User user = ShiroKit.getUser();
        map.put("userId", user.getId());
        PageInfo<Map<String, Object>> pageList = noticeService.getMyNotices(page, limit, map);
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
    }

    /**
     * 撤销通知通告
     * @param ids
     * @return
     */
    @DeleteMapping("/cancelNotices/{ids}")
    public Result cancelNoticesByIds(@PathVariable("ids") String[] ids) {
        noticeService.cancelNoticesByIds(Arrays.asList(ids));
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }

    /**
     * 获取所有通知用户(按角色分类, 以后可能按部门)
     * @param id
     * @return
     */
    @GetMapping("/notice/noticeUserList")
    public Result noticeUserList(@RequestParam String id) {
        //获取所有角色
        List<Map<String, Object>> roles = roleService.getRoles(null);
        //节点数据
        List<Map<String, Object>> list = new ArrayList<>();

        for (Map<String, Object> role : roles) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", role.get("id"));
            data.put("name", role.get("roleName"));
            data.put("pid", 0);
            list.add(data);
            List<Map<String, Object>> users = userService.getUserByRoleId(role.get("id").toString());
            for (Map<String, Object> user : users) {
                Map<String, Object> cData = new HashMap<>();//子节点数据
                cData.put("id", user.get("id"));
                cData.put("name", user.get("userName"));
                cData.put("pid", role.get("id"));

                list.add(cData);
            }
        }
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
    }

    /**
     * 发送通知通告
     * @param noticeVo
     * @return
     */
    @PostMapping("/notice/saveNoticeUser")
    public Result saveNoticeUser(@RequestBody NoticeVo noticeVo) {

        NoticeVo vo = noticeService.saveNoticeUser(noticeVo);

        Map<String, Object> noticeById = noticeService.getNoticeById(noticeVo.getNoticeId());

        //给发送通知的用户推送消息
        Set<String> userIds = noticeVo.getUserIds();
        Map<String, Object> data = new HashMap<>();
        data.put("message", "你收到新的通知通告, 点击查看");
        data.put("priority", noticeById.get("priority"));
        for (String userId : userIds) {
            HashMap<UUID, SocketIOClient> userClient = clientCache.getUserClient(userId);
            if (userClient != null) {
                userClient.forEach((uuid, socketIOClient) -> {
                    //向客户端推送消息
                    socketIOClient.sendEvent("chatEvent", data);
                });
            }
        }
        return new Result(true, ResultEnum.SUCCESS.getValue(), "发送" + ResultEnum.SUCCESS.getMessage(), vo);
    }

    /**
     * 通告标为已读
     */
    @GetMapping("/notice/allMarkReaded")
    public void allMarkReaded() {
        User user = ShiroKit.getUser();
        NoticeUser noticeUser = new NoticeUser();
        noticeUser.setUserId(user.getId());
        noticeUser.setReaded(1);
        noticeUser.setReadedTime(DateUtil.dateToString(new Date(), Constant.DATE_FORMAT_COMMON));
        noticeService.editNoticeUserById(noticeUser);
    }


}
