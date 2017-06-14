package com.fugao.breast.utils.common;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
/**
 * 日期格式转换工具类
 */

public class DateUtils {

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    /**
     * 得到昨天的日期
     *
     * @return
     */
    public static String getBeforeDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 得到今天的时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return sdf.format(new Date());
    }

    /**
     * 得到今天的时间
     *
     * @return
     */
    public static String getCurrentTime1() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }


}
