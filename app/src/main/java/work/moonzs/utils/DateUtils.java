package work.moonzs.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
    //获取当前完整的日期和时间
    public static String getNowDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    //获取当前日期
    public static String getNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // 根据传入的时间，先转换再截取，得到更新时间 传入 "2022-06-25T22:00+08:00"
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String updateTime(String dateTime) {
        String result = null;
        LocalDateTime date = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        // 进一步截取以达到我项目中的需求
        result = dateString.substring(11);
        return result;
    }

    //前一天
    public static String getYesterday(Date date) {
        String tomorrow = "";
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        tomorrow = formatter.format(date);
        return tomorrow;
    }

    //后一天
    public static String getTomorrow(Date date) {
        String tomorrow = "";
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, +1);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        tomorrow = formatter.format(date);
        return tomorrow;
    }

    //获取当前时间
    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    //获取当前日期(精确到毫秒)
    public static String getNowTimeDetail() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    //获取今天是星期几
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //计算星期几
    private static int getDayOfWeek(String dateTime) {

        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    //根据年月日计算是星期几并与当前日期判断  非昨天、今天、明天 则以星期显示
    public static String Week(String dateTime) {
        String week = "";
        String yesterday = "";
        String today = "";
        String tomorrow = "";
        yesterday = getYesterday(new Date());
        today = getNowDate();
        tomorrow = getTomorrow(new Date());

        if (dateTime.equals(yesterday)) {
            week = "昨天";
        } else if (dateTime.equals(today)) {
            week = "今天";
        } else if (dateTime.equals(tomorrow)) {
            week = "明天";
        } else {
            switch (getDayOfWeek(dateTime)) {
                case 1:
                    week = "星期日";
                    break;
                case 2:
                    week = "星期一";
                    break;
                case 3:
                    week = "星期二";
                    break;
                case 4:
                    week = "星期三";
                    break;
                case 5:
                    week = "星期四";
                    break;
                case 6:
                    week = "星期五";
                    break;
                case 7:
                    week = "星期六";
                    break;
            }

        }


        return week;
    }

    //将时间戳转化为对应的时间(10位或者13位都可以)
    public static String formatTime(long time) {
        String times = null;
        if (String.valueOf(time).length() > 10) {// 10位的秒级别的时间戳
            times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000));
        } else {// 13位的秒级别的时间戳
            times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        }
        return times;
    }

    //将时间字符串转为时间戳字符串
    public static String getStringTimestamp(String time) {
        String timestamp = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long longTime = sdf.parse(time).getTime() / 1000;
            timestamp = Long.toString(longTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 时间截取plus
     *
     * @param date 时间
     * @return
     */
    public static String dateSplitPlus(String date) {//2020-08-07
        String result = null;

        String[] array = date.split("-");
        result = Integer.parseInt(array[1]) + "月" + Integer.parseInt(array[2]) + "号";
        return result;
    }
}

