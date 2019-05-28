package com.javaweb.MichaelKai.shiro;

import com.javaweb.MichaelKai.common.properties.ShiroProperties;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: project_base
 * @description: shiro配置
 * @author: YuKai Fan
 * @create: 2019-05-27 14:29
 **/
@Configuration
public class ShiroConfig {

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题
     * 注意：单独一个ShiroFilterFactoryBean配置是报错的,
     * 因为在初始化ShiroFilterFactoryBean时需要注入：SecurityManager
     * Filter Chain定义说明：
     * 1.一个URL可以配置多个Filter,使用逗号分隔
     * 2.当设置多个过滤器,全部验证通过,才视为通过
     * 3.部分过滤器可指定参数,如perms,roles
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //拦截器设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //自定义拦截器
        Map<String, Filter> myFilter = new HashMap<>();
        myFilter.put("auth", new AuthFilter());
        shiroFilterFactoryBean.setFilters(myFilter);

        /**
         *  过滤规则（注意优先级）
         *  —anon 无需认证(登录)可访问
         * 	—authc 必须认证才可访问
         * 	—perms[标识] 拥有资源权限才可访问
         * 	—role 拥有角色权限才可访问
         * 	—user 认证和自动登录可访问
         */
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/login", "anon");
        filterMap.put("/logout", "anon");
        filterMap.put("/noAuth", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/lib/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        //需要将拦截的接口放在最后拦截
        filterMap.put("/**", "auth");

        //这是过滤规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //设置登录页面
        shiroFilterFactoryBean.setLoginUrl("/");
        //未授权错误页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        return shiroFilterFactoryBean;
    }

    /**
     * 配置安全管理器(核心)
     * @param shiroRealm
     * @param sessionManager
     * @param rememberMeManager
     * @return
     */
    @Bean
    public DefaultWebSecurityManager defaultSecurityManager(ShiroRealm shiroRealm, DefaultSessionManager sessionManager, CookieRememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(shiroRealm);
        //设置session管理器
        securityManager.setSessionManager(sessionManager);
        //设置cookie记住我
        securityManager.setRememberMeManager(rememberMeManager);

        return securityManager;
    }

    /**
     * 自定义身份认证realm
     * @param cacheManager
     * @return
     */
    @Bean
    public ShiroRealm shiroRealm(EhCacheManager cacheManager) {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCacheManager(cacheManager);
        return shiroRealm;
    }

    /**
     * 设置shiro缓存Ehcache
     * @return
     */
    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:conf/ehcache-shiro.xml");
        return cacheManager;
    }

    /**
     * session管理器
     * @param cacheManager
     * @param shiroProperties
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager(EhCacheManager cacheManager, ShiroProperties shiroProperties) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheManager);
        sessionManager.setGlobalSessionTimeout(shiroProperties.getGlobalSessionTimeout());
        sessionManager.setSessionValidationInterval(shiroProperties.getSessionValidationInterval());
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.validateSessions();
        //去掉登录页面地址栏的jssessionid
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * rememberMe管理器
     * @param simpleCookie
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie simpleCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode("WcfHGU25gNnTxTlmJMeSpw=="));
        cookieRememberMeManager.setCookie(simpleCookie);
        return cookieRememberMeManager;
    }

    /**
     * 创建一个简单的Cookie对象
     * @param shiroProperties
     * @return
     */
    @Bean
    public SimpleCookie simpleCookie(ShiroProperties shiroProperties) {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        //cookie记住登录信息时间,默认7天
        simpleCookie.setMaxAge(shiroProperties.getRememberMeTimeout() * 24 * 60 * 60);
        return simpleCookie;
    }

    /**
     * 开启shiro aop注解支持.使用代理方式;所以需要开启代码支持；必须要开启注解才能使用@RequiresPermissions等注解
     * @param securityManager
     * @return 授权Advisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}