package com.javaweb.MichaelKai.IM.nettySocketIO;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: project_base
 * @description: chatServer启动类
 * @author: YuKai Fan
 * @create: 2019-06-12 08:51
 **/
@Component
public class PushServer implements InitializingBean {

    @Autowired
    private EventListener eventListener;

    @Value("${netty.server.port}")
    private int serverPort;

    @Value("${netty.server.host-name}")
    private String hostName;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration config = new Configuration();
        config.setPort(serverPort);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        socketConfig.setTcpNoDelay(true);
        socketConfig.setSoLinger(0);

        config.setSocketConfig(socketConfig);
        config.setHostname(hostName);

        SocketIOServer server = new SocketIOServer(config);
        server.addListeners(eventListener);
        server.start();
        System.out.println("启动正常");
    }
}