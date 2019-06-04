package com.javaweb.michaelKai;

import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-04 16:10
 **/
@Component
@ServerEndpoint("/WebSocketServer/{id}")
public class WebSocketServer {
    //静态变量, 用来记录当前在线连接数

}