package com.javaweb.MichaelKai.shiro;

import com.javaweb.MichaelKai.common.constants.AdminConstant;
import com.javaweb.MichaelKai.pojo.User;
import com.javaweb.MichaelKai.service.RoleService;
import com.javaweb.MichaelKai.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description: 自定义realm
 * @author: YuKai Fan
 * @create: 2019-05-27 14:30
 **/
public class ShiroRealm extends AuthorizingRealm{
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取用户主体对象
        User user = (User) principalCollection.getPrimaryPrincipal();

        //获取管理员所有权限
        if (user.getId().equals(AdminConstant.ADMIN_ID)) {
            info.addRole(AdminConstant.ADMIN_ROLE_NAME);
            info.addStringPermission("*:*:*");
            return info;
        }

        //获取角色和权限
        List<Map<String, Object>> roles = userService.getAllRolesByUserId(user.getId());
        for (Map<String, Object> role : roles) {
            info.addRole(role.get("roleName").toString());
            //根据roleId获取所有的权限
            List<Map<String, Object>> permissions = roleService.getAllPermissionsByRoleId(role.get("id").toString());
            for (Map<String, Object> permission : permissions) {
                if (!StringUtils.isEmpty(permission.get("perm")) && !permission.get("perm").toString().contains("*"))
                info.addStringPermission(permission.get("perm").toString());
            }
        }
        return info;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Map<String, Object> map = new HashMap<>();
        map.put("account", token.getUsername());
        List<Map<String, Object>> users = userService.getUsers(map);
        if (users.size() == 0) {
            throw new UnknownAccountException();
        } else if (users.size() > 1) {
            throw new AccountException("账号错误");
        }

        Map<String, Object> user = users.get(0);
        // 对盐进行加密处理
        ByteSource salt = ByteSource.Util.bytes(user.get("salt"));

        /* 传入密码自动判断是否正确
         * 参数1：传入对象给Principal
         * 参数2：正确的用户密码(从数据库中查询的已加密的密码),在下面的自定义密码验证中会进行验证
         * 参数3：加盐处理
         * 参数4：固定写法
         */
        return new SimpleAuthenticationInfo(user, user.get("password"), salt, getName());
    }

    /**
     * 自定义密码验证:
     * 在认证过程中，将密码传入info中。在自定义匹配中取出，并进行加密处理，判断两个密码是否相同
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new SimpleCredentialsMatcher() {
            @Override
            public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
                UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
                SimpleAuthenticationInfo info = (SimpleAuthenticationInfo) authenticationInfo;

                //获取明文密码以及密码盐
                String password = String.valueOf(token.getPassword());
                String account = token.getUsername();
                String salt = CodecSupport.toString(info.getCredentialsSalt().getBytes());//在认证过程中，将加密盐传入认证信息authenticationInfo中

                return equals(ShiroKit.md5(password, account + salt), info.getCredentials());//判断加密密码是否相等
            }
        });
    }
}