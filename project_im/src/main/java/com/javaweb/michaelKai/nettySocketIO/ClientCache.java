package com.javaweb.michaelKai.nettySocketIO;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: project_base
 * @description: 客户端缓存
 * @author: YuKai Fan
 * @create: 2019-06-12 09:10
 **/
@Component
public class ClientCache {

    //本地缓存
    private static Map<String, HashMap<UUID, SocketIOClient>> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 存入本地缓存
     * @param userId 用户id
     * @param sessionId 页面sessionID (防止打开多个页面导致重复)
     * @param socketIOClient 页面对应的通道连接信息
     */
    public void saveClient(String userId, UUID sessionId, SocketIOClient socketIOClient) {
        HashMap<UUID, SocketIOClient> sessionIdClientCache = concurrentHashMap.get(userId);
        if (sessionIdClientCache == null) {
            sessionIdClientCache = new HashMap<>();
        }
        sessionIdClientCache.put(sessionId, socketIOClient);
        concurrentHashMap.put(userId, sessionIdClientCache);
    }

    /**
     * 根据用户id获取所有通道信息
     * @param userId
     * @return
     */
    public HashMap<UUID, SocketIOClient> getUserClient(String userId) {
        return concurrentHashMap.get(userId);
    }

    /**
     * 根据用户id以及sessionID删除页面连接信息
     * @param userId
     * @param sessionId
     */
    public void deleteSessionClient(String userId, UUID sessionId) {
        concurrentHashMap.get(userId).remove(sessionId);
    }

}