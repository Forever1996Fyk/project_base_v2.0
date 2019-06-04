package com.javaweb.MichaelKai.common.utils;

/**
 * @program: project_base
 * @description: byte操作类
 * @author: YuKai Fan
 * @create: 2019-06-04 09:58
 **/
public class BytesUtil {

    /**
     * 用户String.subString对汉字处理存在问题(把一个汉字是为一个字节)
     * 因此在包含汉字的字符串时存在隐患
     * @param str 要截取的字符串
     * @param startIndex 开始坐标(包含)
     * @param endIndex 截止坐标(包含)
     * @return
     */
    public static String substring(String str, int startIndex, int endIndex) {
        byte[] bytes = str.getBytes();
        String target = "";
        for (int i = startIndex; i <= endIndex; i++) {
            target += (char)bytes[i];
        }
        return target;
    }
}