package com.javaweb.MichaelKai.common.utils;

import com.javaweb.MichaelKai.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @program: project_base
 * @description: 日期工具类
 * @author: YuKai Fan
 * @create: 2019-05-28 17:12
 **/
@Slf4j
public class DateUtil {

    /***
     * Date类型转换成XMLGregorianCalendar类型
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        }
        catch (Exception e) {
            log.error("Date类型转换成XMLGregorianCalendar类型出错："+e);
        }
        return gc;
    }

    /***
     * XMLGregorianCalendar类型转换成Date类型
     *
     * @param cal
     * @return
     * @throws Exception
     */
    public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }

    /**
     * String 转 Date
     * @param str
     * @param format
     * @return
     */
    public static Date stringToDate(String str, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
        }
        catch (ParseException e) {
            log.error("String类型 转 Date类型出错："+e);
        }
        return date;
    }

    /**
     * Date 转 String
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate=null;
        try {
            if(date!=null){
                strDate=dateFormat.format(date);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Date类型 转 String类型出错："+e);
        }
        return strDate;
    }

    /**
     * 将long类型转化为Date
     * @param time
     * @return
     */
    public static Date longToDate(long time) {
        return new Date(time * 1000);
    }

    /**
     * 将long类型转为String
     * @param time
     * @return
     */
    public static String longToString(long time) {
        return dateToString(longToDate(time), Constant.DATE_FORMAT_COMMON);
    }

    /**
     * 计算两个日期相差多少天
     * @param startDate
     * @param endDate
     * @return
     */
    public static int differDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long to = 0, from = 0;
        try {
            to = sdf.parse(endDate).getTime();
            from = sdf.parse(startDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int num = (int) ((to - from) / (1000 * 60 *60 *24));
        return num;
    }

}