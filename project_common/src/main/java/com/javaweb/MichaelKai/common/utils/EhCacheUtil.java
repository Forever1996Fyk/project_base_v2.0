package com.javaweb.MichaelKai.common.utils;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;

/**
 * @program: project_base
 * @description: EhCache缓存操作工具
 * @author: YuKai Fan
 * @create: 2019-05-24 15:06
 **/
public class EhCacheUtil {

    /**
     * 获取EhCacheManager管理对象
     */
    public static EhCacheManager getCacheManager(){
        return SpringContextUtil.getBean(EhCacheManager.class);
    }

    /**
     * 获取EhCache缓存对象
     */
    public static Cache getCache(String name){
        return getCacheManager().getCache(name);
    }

    /**
     * 获取字典缓存对象
     */
    public static Cache getDictCache(){
        return getCacheManager().getCache("dictionary");
    }

}
