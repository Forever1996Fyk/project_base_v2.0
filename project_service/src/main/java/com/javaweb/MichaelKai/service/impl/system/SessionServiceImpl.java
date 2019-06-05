package com.javaweb.MichaelKai.service.impl.system;

import com.javaweb.MichaelKai.common.constants.Constant;
import com.javaweb.MichaelKai.common.utils.DateUtil;
import com.javaweb.MichaelKai.entity.UserOnline;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.system.SessionService;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:
 * @author: YuKai Fan
 * @create: 2019-06-05 17:01
 **/
@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionDAO sessionDAO;

    /**
     * 通过SessionDao的getActiveSessions()方法，我们可以获取所有有效的Session，通过该Session，我们还可以获取到当前用户的Principal信息。
     * 值得说明的是，当某个用户被踢出后（Session Time置为0），该Session并不会立刻从ActiveSessions中剔除，所以我们可以通过其timeout信息来判断该用户在线与否。
     * @return
     */
    @Override
    public List<UserOnline> list() {
        List<UserOnline> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            UserOnline userOnline = new UserOnline();
            User user = new User();
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (User) principalCollection.getPrimaryPrincipal();
                userOnline.setNickName(user.getNickName());
                userOnline.setUserId(user.getId());
            }

            userOnline.setId(session.getId().toString());
            userOnline.setHost(session.getHost());
            userOnline.setSessionStartTime(DateUtil.dateToString(session.getStartTimestamp(), Constant.DATE_FORMAT_CREATE_UPDATE));
            userOnline.setLastAccessTime(DateUtil.dateToString(session.getLastAccessTime(), Constant.DATE_FORMAT_CREATE_UPDATE));
            long timeout = session.getTimeout();
            if (timeout == 0l) {
                userOnline.setStatus(0);
            } else {
                userOnline.setStatus(1);
            }
            userOnline.setTimeout(timeout);
            list.add(userOnline);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> userOnlineList() {
        return null;
    }

    @Override
    public boolean forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        return true;
    }
}