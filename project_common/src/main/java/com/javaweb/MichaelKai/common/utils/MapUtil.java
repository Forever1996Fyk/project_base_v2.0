package com.javaweb.MichaelKai.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import static com.javaweb.MichaelKai.common.utils.FormUtil.camelToUnderline;

/**
 * @program: project_base
 * @description: map转换
 * @author: YuKai Fan
 * @create: 2019-05-21 11:14
 **/
public class MapUtil {

    /**
     * @Description 将map对象转为JavaBean
     *
     * @Author YuKai Fan
     * @Date 22:15 2019/8/12
     * @Param
     * @return
     **/
    public static <T> T mapToObject(Class<T> clazz, Map<String, Object> map, boolean delUnderLine) {
        try {
            T obj = clazz.newInstance(); //创建JavaBean对象
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz); //获取类属性
            setPropertyValue(obj, beanInfo, map, delUnderLine);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Description 将map对象转为JavaBean
     *
     * @Author YuKai Fan
     * @Date 22:15 2019/8/12
     * @Param
     * @return
     **/
    public static <T> T mapToObject(T obj, Map<String, Object> map, boolean delUnderLine) {
        try {
            Class<?> clazz = obj.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
            setPropertyValue(obj, beanInfo, map, delUnderLine);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @Description 将bean转为map
     *
     * @Author YuKai Fan
     * @Date 22:33 2019/8/12
     * @Param
     * @return
     **/
    public static <T> Map<String, Object> objectToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        return objectToMap(bean, map);
    }

    /**
     * @Description 将bean属性追加到已存在的map中
     *
     * @Author YuKai Fan
     * @Date 22:33 2019/8/12
     * @Param
     * @return
     **/
    public static <T> Map<String, Object> objectToMap(T bean, Map<String, Object> map) {
        if (bean == null) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                //过滤key的属性
                if (!key.equals("class")) {
                    //得到property相应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("transBean2Map Error " + e);
        }

        return map;
    }

    private static <T> void setPropertyValue(T obj, BeanInfo beanInfo, Map<String, Object> map, boolean delUnderLine) throws ParseException {
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            descriptor.getPropertyType();
            String propertyName = delUnderLine ? camelToUnderline(descriptor.getName()) : descriptor.getName();
            if (!map.containsKey(propertyName) || null==map.get(propertyName)) {
                continue;
            }
            // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
            try {
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args = setValue(descriptor, args, value);
                descriptor.getWriteMethod().invoke(obj, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Object[] setValue(PropertyDescriptor descriptor, Object[] args, Object mapValue) throws ParseException {
        if (descriptor.getPropertyType() == Integer.class || descriptor.getPropertyType() == int.class) {
            if (mapValue instanceof Integer) {
                args[0] = mapValue;
            } else {
                if(StringUtils.isNotBlank(mapValue.toString())){
                    args[0] = Integer.parseInt(mapValue.toString());
                }
            }
            // 当set方法中的参数为Date
        }else if (descriptor.getPropertyType() == Byte.class || descriptor.getPropertyType() == byte.class) {
            if (mapValue instanceof Byte) {
                args[0] = mapValue;
            } else {
                if(StringUtils.isNotBlank(mapValue.toString())){
                    args[0] = Byte.parseByte(mapValue.toString());
                }
            }
            // 当set方法中的参数为Float
        }else if (descriptor.getPropertyType() == BigDecimal.class) {
            if (mapValue instanceof BigDecimal) {
                args[0] = mapValue;
            } else {
                if(StringUtils.isNotBlank(mapValue.toString())){
                    args[0] = new BigDecimal(mapValue.toString());
                }
            }
            // 当set方法中的参数为Float
        } else if (descriptor.getPropertyType() == Date.class || descriptor.getPropertyType() == java.sql.Date.class) {
            if (mapValue instanceof Date) {
                args[0] = mapValue;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                args[0] = sdf.parse((String) mapValue);
            }
            // 当set方法中的参数为Float
        }else if (descriptor.getPropertyType() == double.class || descriptor.getPropertyType() == Double.class) {
            if (mapValue instanceof Double) {
                args[0] = mapValue;
            } else {
                if(StringUtils.isNotBlank(mapValue.toString())){
                    args[0] = Double.parseDouble((String) mapValue);
                }
            }
            // 当set方法中的参数为其他
        } else if (descriptor.getPropertyType() == String.class) {

            if (mapValue instanceof String[]) {

                String[] tempArray = (String[]) mapValue;
                String result = "";
                for (int m = 0; m < tempArray.length; m++) {
                    result = result + tempArray[m] + ",";
                }
                result = result.substring(0, result.length() - 1);
                args[0] = result;

            } else {
                args[0] = (String) mapValue;
            }
        }
        return args;
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