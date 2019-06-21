package com.javaweb.MichaelKai.controller.system;

import com.corundumstudio.socketio.SocketIOClient;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.entity.MonitorInfoEntity;
import com.javaweb.MichaelKai.service.MonitorService;
import com.javaweb.MichaelKai.service.SessionService;
import com.javaweb.MichaelKai.IM.nettySocketIO.ClientCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-04 10:23
 **/
@RestController
@RequestMapping("/api/system")
public class SystemController {
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ClientCache clientCache;
    /**
     * 获取当前系统信息
     * @return
     * @throws Exception
     */
    @GetMapping("/getMonitor")
    public Result getMonitor() throws Exception {
        MonitorInfoEntity monitorInfo = monitorService.getMonitorInfo();
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), monitorInfo);
    }

    /**
     * 获取在线人员
     * @return
     */
    @GetMapping("/getOnlineUsers")
    public Result getOnlineUsers() {
        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), sessionService.list());
    }

    /**
     * 推送消息
     * @param userId
     * @return
     */
    @GetMapping("/push/{userId}")
    public Result pushToUser(@PathVariable("userId") String userId) {
        HashMap<UUID, SocketIOClient> userClient = clientCache.getUserClient(userId);
        userClient.forEach((uuid, socketIOClient) -> {
            //向客户端推送消息
            socketIOClient.sendEvent("chatEvent", "推送消息");
        });

        return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage());
    }
}