package com.vigorchip.omatreadmill.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wr-app1 on 2018/4/25.
 * 处理获取倒数模式设置的时间的工具类
 */

public class TimeUtils {
    private static int hh0;
    private static int hh1;
    private static int mm2;
    private static int mm3;
    private static int timer;
    private static char[] arr_time;

    public static int TimeManager(String time) {
        arr_time = time.toCharArray();
        if (arr_time.length != 4) {
            return 0000000;
        }
        hh0 = (CharToInt(arr_time[0])) * ((60 * 60) * 10);
        hh1 = (CharToInt(arr_time[1])) * 60 * 60;
        mm2 = (CharToInt(arr_time[2])) * 60 * 10;
        mm3 = (CharToInt(arr_time[3])) * 60;
        timer = hh0 + hh1 + mm2 + mm3;
        return timer;
    }

    public static int CharToInt(char arg0) {//字符转int
        char numChar = arg0;
        int intNum = numChar - '0';
        return intNum;
    }

    public static int RacTime(String time) {//返回每条能量柱所需要的时间
        int RacTime = TimeUtils.TimeManager(time) / 10;
        return RacTime;
    }

    public static String TimeFomat(long time) {//返回时间格式的字符串
        String hh = new DecimalFormat("00").format(time / 3600);
        String mm = new DecimalFormat("00").format(time % 3600 / 60);
        String ss = new DecimalFormat("00").format(time % 60);
        String strTime = new String(hh + ":" + mm + ":" + ss);
        return strTime;
    }

    public static int MinusTime(int Time, int inTime) {//总时间减去一条能量柱时间等于一体能量柱最后的时间
        int time = Time - inTime;
        return time;
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 时间戳转日期
     *
     * @param ms
     * @return
     */
    public static String transForDate(String ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String sd = sdf.format(new Date(Long.parseLong(ms)));
        return sd;

    }

    /**
     * 时间戳转时分秒
     * HH:mm:ss
     * @param timetemp
     * @return
     */
    public static String getHMS(long timetemp) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(timetemp);
        SimpleDateFormat fmat=new SimpleDateFormat("HH:mm:ss");
        String time=fmat.format(calendar.getTime());
        return time;
    }
}
