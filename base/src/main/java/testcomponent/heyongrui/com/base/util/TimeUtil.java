package testcomponent.heyongrui.com.base.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lambert on 2018/11/5.
 */

public class TimeUtil {
    public final static String SEC = "yyyy-MM-dd HH:mm:ss";
    public final static String MIN = "yyyy-MM-dd HH:mm";
    public final static String MIN_TWO = "MM-dd HH:mm";
    public final static String HOURS_MIN = "HH:mm";
    public final static String DAY_ONE = "yyyy-MM-dd";
    public final static String DAY_TWO = "yyyy.MM.dd";
    public final static String DAY_THREE = "MM-dd";
    public final static String DAY_FOUR = "MM月dd日";
    public final static String DAY_FIVE = "yyyy年MM月dd日";
    public final static long nd = 1000 * 24 * 60 * 60;
    public final static long nh = 1000 * 60 * 60;
    public final static long nm = 1000 * 60;
    public final static long ns = 1000;

    /**
     * 将日期转成指定格式的日期字符串
     */
    public static String getDateString(Date date, String targetDateType) {
        if (date == null) {
            throw new NullPointerException("date is NullPointerException");
        }
        if (TextUtils.isEmpty(targetDateType)) {
            throw new NullPointerException("please set a target date format");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(targetDateType);
        String datestring = sdf.format(date);
        return datestring;
    }

    /**
     * 判断是否是下午
     */
    public static boolean isPM() {
        GregorianCalendar ca = new GregorianCalendar();
        int isPM = ca.get(GregorianCalendar.AM_PM);
        return isPM == 1;
    }

    /**
     * 获取指定日期的星期几(0...6代表周日到周六)
     */
    public static int getWeek(Date date) {
        if (date == null) {
            throw new NullPointerException("date is NullPointerException");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
        return weekday < 0 ? 0 : weekday;
    }
}
