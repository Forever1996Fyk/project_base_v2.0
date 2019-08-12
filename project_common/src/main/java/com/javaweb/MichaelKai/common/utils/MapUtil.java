package com.javaweb.MichaelKai.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
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
     * 将实体转为map
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }

        return new BeanMap(obj);
    }

    /**
     * 从request中获取参数Map，并返回可读的Map
     * @param request
     * @return
     */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        //参数Map
        Map properties = request.getParameterMap();
        //返回值map
        Map<String, Object> returnMap = Maps.newHashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (valueObj == null) {
                value = null;
            } else if (valueObj instanceof String[]) {
                value = "";
                String[] values = (String[]) valueObj;
                for (int i = 0;i < values.length; i++) {
                    if ("null".equalsIgnoreCase(values[i])) {
                        value = value + ",";
                    } else {
                        value = values[i] + ",";
                    }
                }
                value = value.substring(0, value.length() - 1);
            } else if ("null".equalsIgnoreCase(valueObj.toString())) {
                value = null;
            } else {
                value = valueObj.toString();
            }

            returnMap.put(name, value);
        }

        return returnMap;
    }
}