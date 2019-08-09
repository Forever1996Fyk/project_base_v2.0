package com.javaweb.MichaelKai.common.utils;

import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName Base64Util
 * @Description TODO
 * @Author YuKai Fan
 * @Date 2019/8/9 20:28
 * @Version 1.0
 **/
public class Base64Util {
    private static BASE64Encoder encoder = new BASE64Encoder();

    public static String ioToBase64(InputStream in) {
        String base64 = null;

        try {
            byte[] bytes = new byte[in.available()];
            //将文件中的内容读入到数组中
            in.read(bytes);
            base64 = encoder.encode(bytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return base64;
    }

}
