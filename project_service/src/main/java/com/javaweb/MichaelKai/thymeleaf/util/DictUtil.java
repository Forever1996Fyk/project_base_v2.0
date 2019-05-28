package com.javaweb.MichaelKai.thymeleaf.util;

import com.javaweb.MichaelKai.common.utils.EhCacheUtil;
import com.javaweb.MichaelKai.common.utils.SpringContextUtil;
import com.javaweb.MichaelKai.service.BasicDictService;
import org.apache.shiro.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: project_base
 * @description:  字典提取工具对象
 * @author: YuKai Fan
 * @create: 2019-05-24 15:06
 **/
public class DictUtil {

    //必须要先开启cache,可以使用redis,也可以使用ehcache
    private static Cache dictCache = EhCacheUtil.getDictCache();

    /**
     * 获取字典值集合
     * @param label 字典标识
     */
    public static Map<String, String> value(String label){
        Map<String, String> value = null;

        Object obj = dictCache.get(label);
        if (obj != null) {
            value =  (Map<String, String>) obj;
        } else {
            BasicDictService basicDictService = SpringContextUtil.getBean(BasicDictService.class);
            Map<String, Object> map = new HashMap<>();
            map.put("code", label);
            List<Map<String, Object>> dictItems = basicDictService.getDictItem(map);
            if (dictItems.size() > 0) {
                value = new LinkedHashMap<>();
                for (Map<String, Object> dictItem : dictItems) {
                    value.put(dictItem.get("itemCode").toString(), dictItem.get("itemName").toString());
                }
            }
            dictCache.put(label, value);
        }

        return value;
    }

    /**
     * 根据选项编码获取选项值
     * @param label 字典标识
     * @param code 选项编码
     */
    public static String keyValue(String label, String code){
        Map<String, String> list = DictUtil.value(label);
        if(list != null){
            return list.get(code);
        }else{
            return "";
        }
    }

    /**
     * 封装数据状态字典
     * @param status 状态
     */
    public static String dataStatus(Byte status){
        String label = "DATA_STATUS";
        return DictUtil.keyValue(label, String.valueOf(status));
    }

    /**
     * 清除缓存中指定的数据
     * @param label 字典标识
     */
    public static void clearCache(String label){
        Object o = dictCache.get(label);
        if (o != null){
            dictCache.remove(label);
        }
    }
}
