package com.yunma.utils;

import java.text.*;
import java.util.*;

/**
 * Created by Json on 12/05/2016.
 */
public class DateTimeUtils {
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT //年-月-日 时:分:秒
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_Y_M_D_H_M //年-月-日 时:分
            = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE    //年-月-日
            = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static final SimpleDateFormat YEAR_MONTH_DAY    //年-月-日
            = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
    public static final SimpleDateFormat YEAR_MONTH          //年-月
            = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    public static final SimpleDateFormat MONTH_DAY           //月-日
            = new SimpleDateFormat("MM-dd", Locale.CHINA);
    public static final SimpleDateFormat YEAR                //年
            = new SimpleDateFormat("yyyy", Locale.CHINA);
    public static final SimpleDateFormat MONTH               //月
            = new SimpleDateFormat("MM", Locale.CHINA);
    public static final SimpleDateFormat DD                  //日
            = new SimpleDateFormat("dd", Locale.CHINA);
    public static final SimpleDateFormat HHMM                //时分
            = new SimpleDateFormat("HHmm", Locale.CHINA);
    public static final SimpleDateFormat HH_MM                //时分
            = new SimpleDateFormat("HH:mm", Locale.CHINA);
    public static final SimpleDateFormat HH                  //时
            = new SimpleDateFormat("HH", Locale.CHINA);

    private DateTimeUtils() {
        throw new AssertionError();
    }


    public static int getCurrentDay(){
       return Integer.valueOf(getTime(getCurrentTimeInLong(),DD));
    }

    /**
     * long time to string
     * 传入毫秒类型的数值和你要转换的时间格式，返回相应的时间格式的时间值
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 将毫秒类型的时间值转换成默认时间格式的字符串类型的时间值
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间的毫秒值
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间，是字符串类型的，默认的时间格式化的一个时间值
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 根据你传进去的时间格式，获取到相应你想要的时间格式值
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        if(date != null){
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekOfDays[w];
    }

    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return DATE_FORMAT_DATE.format(c.getTime());
    }

    public static int getAge(long time) throws Exception {

        Date birthDay = new Date(time);
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay))
        {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth)
        {
            if (monthNow == monthBirth)
            {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            }
            else
            {
                age--;
            }
        }
        return age;
    }

    /**
     * 计算时间差
     * @param starTime 开始时间
     * @param endTime  结束时间
     * 返回类型 ==1----天，时，分。 ==2----时
     * @return 返回时间差
     */
    public static String getTimeDifference(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
            long hour1 = diff / (60 * 60 * 1000);
            String hourString = hour1 + "";
            long min1 = ((diff / (60 * 1000)) - hour1 * 60);
            timeString = hour1 + "小时" + min1 + "分";
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeString;

    }

    /**
     * 计算相差的小时
     * @param starTime
     * @param endTime
     * @return
     */
    public static String getTimeDifferenceHour(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            String string = Long.toString(diff);

            float parseFloat = Float.parseFloat(string);

            float hour1 = parseFloat / (60 * 60 * 1000);

            timeString = Float.toString(hour1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeString;

    }

    /**
     * 计算毫秒差
     *
     * @param starTime
     *            开始时间
     * @param endTime
     *            结束时间
     * 返回类型 ==1----天，时，分。 ==2----时
     * @return 返回时间差
     */
    public static Integer getTimeDifferenceSecond(String starTime, String endTime) {
        Integer mSecond = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS",Locale.CHINA);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            mSecond = (int) ms;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mSecond;

    }

}
