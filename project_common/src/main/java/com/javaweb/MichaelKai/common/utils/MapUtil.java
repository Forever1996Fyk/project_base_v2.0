package com.javaweb.MichaelKai.common.utils;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @program: project_base
 * @description: map转换
 * @author: YuKai Fan
 * @create: 2019-05-21 11:14
 **/
public class MapUtil {

    /**
     * 将map转为实体
     * @param beanClass
     * @param map
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Class<?> beanClass, Map<String, Object> map) throws Exception {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }

        //利用反射获取实体对象
        Object obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * 将尸体转为map
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        return new BeanMap(obj);
    }
}