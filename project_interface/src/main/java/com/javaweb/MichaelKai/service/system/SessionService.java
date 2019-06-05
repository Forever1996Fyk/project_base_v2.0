package com.javaweb.MichaelKai.service.system;

import com.javaweb.MichaelKai.entity.UserOnline;

import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: session接口
 * @author: YuKai Fan
 * @create: 2019-06-05 16:59
 **/
public interface SessionService {

    /**
     * 在线用户
     * @return
     */
    List<UserOnline> list();

    /**
     * 在线用户列表
     * @return
     */
    List<Map<String, Object>> userOnlineList();

    /**
     * 是否强制下线
     * @param sessionId
     * @return
     */
    boolean forceLogout(String sessionId);
}