package testcomponent.heyongrui.com.base.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
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

    public static boolean isPM() {
        GregorianCalendar ca = new GregorianCalendar();
        int isPM = ca.get(GregorianCalendar.AM_PM);
        return isPM == 1;
    }
}
