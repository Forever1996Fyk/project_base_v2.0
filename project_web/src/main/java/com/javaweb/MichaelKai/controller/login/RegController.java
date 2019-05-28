package com.javaweb.MichaelKai.controller.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: project_base
 * @description: 注册controller
 * @author: YuKai Fan
 * @create: 2019-05-28 08:56
 **/
@RestController
public class RegController {

    @GetMapping("/reg")
    public String toReg() {
        return "reg";
    }
}