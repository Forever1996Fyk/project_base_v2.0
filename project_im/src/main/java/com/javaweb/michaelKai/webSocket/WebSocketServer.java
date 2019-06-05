package com.javaweb.michaelKai.webSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.javaweb.MichaelKai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-04 16:10
 **/
@Component
@ServerEndpoint("/webSocketServer/{userId}")
@Slf4j
public class WebSocketServer {
    //静态变量, 用来记录当前在线连接数
    public static int onlineNumber = 0;
    /**
     * 用户id为key,WebSocket为对象保存
     */
    private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户id
     */
    private String userId;

    @Autowired
    private UserService userService;

    /**
     * 建立连接
     * @param userId
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        onlineNumber++;
        Map<String, Object> user = userService.getUserById(userId);
        String userName = user.get("userName").toString();
        log.info("连接的客户id：" + session.getId() + "数据库的用户名：" + userName);
        this.userId = userId;
        this.session = session;
        log.info("有新连接加入! 当前在线人数：" + onlineNumber);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            Map<String, Object> map = new HashMap<>();
            map.put("messageType", 1);
            map.put("userName", userName);
            sendMessageAll(JSON.toJSONString(map), userId);

            //将自己的信息加入到map中
            clients.put(userName, this);
            //给自己发送消息:告诉自己现在都有谁在线
            Map<String, Object> map1 = new HashMap<>();
            map1.put("messageType", 3);
            //移除自己
            Set<String> set = clients.keySet();
            map1.put("onlineUsers", set);
            sendMessageTo(JSON.toJSONString(map1), userId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(userName + "上线的时候通知所有人发生错误");
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        clients.remove(userId);

        Map<String, Object> user = userService.getUserById(userId);
        String userName = user.get("userName").toString();
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            Map<String, Object> map = new HashMap<>();
            map.put("messageType", 2);
            map.put("userName", userName);
            sendMessageAll(JSON.toJSONString(map), userId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(userName + "下线的时候通知所有人发生错误");
        }

        log.info("有连接关闭！当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息：" + message + "客户端id：" + session.getId());
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("message");
            String fromUserName = jsonObject.getString("userName");
            String toUserName = jsonObject.getString("to");
            //如果不是发给所有，那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String,Object> map1 =new HashMap<String,Object>();
            map1.put("messageType",4);
            map1.put("textMessage",textMessage);
            map1.put("fromUserName",fromUserName);
            if (toUserName.equals("All")) {
                map1.put("toUserName", "所有人");
                sendMessageAll(JSON.toJSONString(map1), fromUserName);
            } else {
                map1.put("toUserName", toUserName);
                sendMessageTo(JSON.toJSONString(map1), toUserName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("发生了错误了");
        }
    }

    public static synchronized int getOnlineNumber() {
        return onlineNumber;
    }

    @OnError
    public void OnError(Session session, Throwable throwable) {
        log.error("服务端发生了错误" + throwable.getMessage());
    }

    /**
     * 单发消息
     * @param message
     * @param userId
     */
    private void sendMessageTo(String message, String userId) throws IOException {
        for (WebSocketServer item : clients.values()) {
            if (item.userId.equals(userId)) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    /**
     * 群发消息
     * @param message
     * @param userName
     */
    private void sendMessageAll(String message, String userName) throws IOException {
        for (WebSocketServer item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

}