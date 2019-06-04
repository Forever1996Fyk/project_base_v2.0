package com.javaweb.MichaelKai.controller.system;

import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.entity.MonitorInfoEntity;
import com.javaweb.MichaelKai.service.system.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}