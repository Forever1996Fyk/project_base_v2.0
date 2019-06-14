package com.javaweb.MichaelKai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: project_base
 * @description: 工作流controller
 * @author: YuKai Fan
 * @create: 2019-06-14 16:03
 **/
@RestController
@RequestMapping("/api/activiti")
public class ActivitiController {

    @RequestMapping("/create")
    public void create(HttpServletRequest request, HttpServletResponse response) {
    }
}