package com.change.hippo.utils.date;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    public static String DATE_TYPE_YYYY_MM_DD_HHMISS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间串转换成 时间格式
     *
     * @param date
     * @param format 为空则默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String date, String format) {
        if (StringUtils.isBlank(format))
            format = DATE_TYPE_YYYY_MM_DD_HHMISS;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回当前时间的字符串
     *
     * @return
     */
    public static String getCurrentDateString() {
        return dateToString(new Date(), null);
    }

    /**
     * 时间格式转换成字符串
     *
     * @param date
     * @param format 为空则默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (StringUtils.isBlank(format))
            format = DATE_TYPE_YYYY_MM_DD_HHMISS;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date addDateOneDay(Date date) {
        if (null == date) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.DATE, -7); //日期减7天
        date = c.getTime();
        return date;
    }

    public static String addDateDay(int day) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.DATE, day); //日期减7天
        date = c.getTime();
        return getStringTimeByPattern(date, DATE_TYPE_YYYY_MM_DD_HHMISS);
    }

    public static String getStringTimeByPattern(Date date, String formatType) {
        if (date == null) {
            date = new Date();
        }
        return new SimpleDateFormat(formatType).format(date);
    }


    public static void main(String[] args) {
        System.out.println("20170313".substring(0, 4));
    }
}