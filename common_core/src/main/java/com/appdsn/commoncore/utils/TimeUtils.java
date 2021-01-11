/*
 *
 */
package com.appdsn.commoncore.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Desc:日期时间获取，格式化
 *
 * @Author: wangbaozhong 2019/10/28 16:31
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class TimeUtils {

    public static final int UNIT_MSEC = 1;
    public static final int UNIT_SEC = 1000;
    public static final int UNIT_MIN = 60000;
    public static final int UNIT_HOUR = 3600000;
    public static final int UNIT_DAY = 86400000;

    private static final String DEFAULT_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static SimpleDateFormat getDefaultFormat() {
        return getDateFormat(DEFAULT_FORMAT_PATTERN);
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            return getDefaultFormat();
        }
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        try {
            if (simpleDateFormat == null) {
                simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
                SDF_THREAD_LOCAL.set(simpleDateFormat);
            } else {
                simpleDateFormat.applyPattern(pattern);
            }
            return simpleDateFormat;
        } catch (Exception e) {
        }
        return simpleDateFormat;
    }

    /**
     * 时间格式化成字符串
     */
    public static String formatDate(long millisTime) {
        return formatDate(millisTime, getDefaultFormat());
    }

    /**
     * 时间格式化成字符串
     */
    public static String formatDate(long millisTime, String pattern) {
        return formatDate(millisTime, getDateFormat(pattern));
    }

    /**
     * 时间格式化成字符串
     */
    public static String formatDate(final long millisTime, final DateFormat format) {
        if (format != null) {
            return format.format(new Date(millisTime));
        }
        return "";
    }


    /**
     * 时间格式化成字符串
     */
    public static String formatDate(Date date) {
        return formatDate(date, getDefaultFormat());
    }

    /**
     * 时间格式化成字符串
     */
    public static String formatDate(Date date, String pattern) {
        return formatDate(date, getDateFormat(pattern));
    }

    /**
     * 时间格式化成字符串
     */
    public static String formatDate(Date date, final DateFormat format) {
        if (format != null) {
            return format.format(date);
        }
        return "";
    }

    /**
     * String类型的日期时间转化为Date类型.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param dateStr The formatted time string.
     * @return the date
     */
    public static Date string2Date(final String dateStr) {
        return string2Date(dateStr, getDefaultFormat());
    }

    /**
     * String类型的日期时间转化为Date类型.
     *
     * @param dateStr The formatted time string.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the date
     */
    public static Date string2Date(final String dateStr, final String pattern) {
        return string2Date(dateStr, getDateFormat(pattern));
    }

    /**
     * String类型的日期时间转化为Date类型..
     *
     * @param dateStr The formatted time string.
     * @param format  The format.
     * @return the date
     */
    public static Date string2Date(final String dateStr, final DateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date to the milliseconds.
     *
     * @param date The date.
     * @return the milliseconds
     */
    public static long date2MillisTime(final Date date) {
        return date.getTime();
    }

    /**
     * Milliseconds to the date.
     *
     * @param millisTime The milliseconds.
     * @return the date
     */
    public static Date millisTime2Date(final long millisTime) {
        return new Date(millisTime);
    }


    /**
     * 返回的时间跨度, 用给定的时间单位
     *
     * @param startDate1 The first date.
     * @param endDate2   The second date.
     * @param unit       The unit of time span.
     *                   <ul>
     *                   <li>{TimeUtils.UNIT_MSEC}</li>
     *                   <li>{TimeUtils.UNIT_SEC }</li>
     *                   <li>{TimeUtils.UNIT_MIN }</li>
     *                   <li>{TimeUtils.UNIT_HOUR}</li>
     *                   <li>{TimeUtils.UNIT_DAY }</li>
     *                   </ul>
     * @return the time span, in unit
     */
    public static long getTimeSpan(final Date startDate1, final Date endDate2, final int unit) {
        return getTimeSpan(endDate2.getTime(), startDate1.getTime(), unit);
    }

    /**
     * 返回的时间跨度, 用给定的时间单位
     *
     * @param startTime1 The first milliseconds.
     * @param endTime2   The second milliseconds.
     * @param unit       The unit of time span.
     *                   <ul>
     *                   <li>{ TimeUtils.UNIT_MSEC}</li>
     *                   <li>{ TimeUtils.UNIT_SEC }</li>
     *                   <li>{ TimeUtils.UNIT_MIN }</li>
     *                   <li>{ TimeUtils.UNIT_HOUR}</li>
     *                   <li>{ TimeUtils.UNIT_DAY }</li>
     *                   </ul>
     * @return the time span, in unit
     */
    public static long getTimeSpan(final long startTime1, final long endTime2, int unit) {
        return (endTime2 - startTime1) / unit;
    }

    /**
     * 返回以毫秒为单位的当前时间;
     */
    public static long getNowMillsTime() {
        return System.currentTimeMillis();
    }

    /**
     * 描述：计算两个日期所差的自然天数.
     *
     * @param millisTime1 第一个时间的毫秒表示
     * @param millisTime2 第二个时间的毫秒表示
     * @return int 所差的天数
     */
    public static int getOffsetDay(long millisTime1, long millisTime2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(millisTime1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(millisTime2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param millisTime1 第一个时间的毫秒表示
     * @param millisTime2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffsetHour(long millisTime1, long millisTime2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(millisTime1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(millisTime2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffsetDay(millisTime1, millisTime2);
        h = h1 - h2 + day * 24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param millisTime1 第一个时间的毫秒表示
     * @param millisTime2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffsetMinutes(long millisTime1, long millisTime2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(millisTime1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(millisTime2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffsetHour(millisTime1, millisTime2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }

    /**
     * 描述：获取偏移之后的Date.
     *
     * @param date          日期时间
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 描述：获取偏移之后的Date.
     *
     * @param dateStr       String形式的日期时间
     * @param pattern       格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public static Date getDateByOffset(String dateStr, String pattern, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            SimpleDateFormat dateFormat = getDateFormat(pattern);
            c.setTime(dateFormat.parse(dateStr));
            c.add(calendarField, offset);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 取指定日期为星期几
     *
     * @param dateStr 指定日期
     * @param pattern 指定日期格式
     * @return String   星期几
     */
    public static String getWeekNumber(String dateStr, String pattern) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            calendar.setTime(df.parse(dateStr));
        } catch (Exception e) {
            return week;
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 是否今天
     */
    public static boolean isToday(final String dateStr, final String pattern) {
        return isToday(string2Date(dateStr, pattern));
    }

    /**
     * 是否今天
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isToday(final Date date) {
        return isToday(date.getTime());
    }

    /**
     * 是否今天
     *
     * @param millisTime The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isToday(final long millisTime) {
        long wee = getZeroOfToday();
        return millisTime >= wee && millisTime < wee + UNIT_DAY;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param millisTime 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true昨天 false不是
     */
    public static boolean isYesterday(long millisTime) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(millisTime);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);
            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为同一天
     *
     * @param time1 时间丑
     */
    public static boolean isSameDay(long time1, long time2) {
        Date date1 = new Date();
        date1.setTime(time1);

        Date date2 = new Date();
        date2.setTime(time2);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
            return true;

        }
        return false;
    }


    /**
     * 判断是否为同一个月
     *
     * @param time1 时间丑
     */
    public static boolean isSameMonth(long time1, long time2) {
        Date date1 = new Date();
        date1.setTime(time1);

        Date date2 = new Date();
        date2.setTime(time2);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {
            return true;

        }
        return false;
    }

    /**
     * 判断是否是闰年
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLeapYear(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 判断是否是闰年
     *
     * @param millisTime The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLeapYear(final long millisTime) {
        return isLeapYear(millisTime2Date(millisTime));
    }

    /**
     * 判断是否是闰年
     *
     * @param year The year.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isLeapYear(final int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }


    // 获取当前年
    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    // 获取当前月份
    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    // 获取当前月的第几天
    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 描述：获取本周的某一天.
     *
     * @param format        the format
     * @param calendarField the calendar field {Calendar.SUNDAY, Calendar.MONDAY}
     * @return String String类型日期时间
     */
    public static String getDayOfWeek(String format, int calendarField) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            } else {
                int offectDay = calendarField - week;
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay);
                }
                c.add(Calendar.DATE, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：获取本月第一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            //当前月的第一天
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：获取本月最后一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的最后一天
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    /**
     * Return the friendly time span by now.
     *
     * @param lastMillisTime The milliseconds.
     * @return the friendly time span by now
     * <ul>
     * <li>如果在 1 分钟内，显示 刚刚</li>
     * <li>如果在 1 小时内，显示 XXX分钟前</li>
     * <li>如果在 1 小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(final long lastMillisTime) {
        long now = System.currentTimeMillis();
        long span = now - lastMillisTime;
        if (span < 0) {
            return String.format("%tc", lastMillisTime);
        }

        if (span < UNIT_MIN) {
            return "刚刚";
        } else if (span < UNIT_HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / UNIT_MIN);
        } else if (span < UNIT_DAY) {
            return String.format(Locale.getDefault(), "%d小时前", span / UNIT_HOUR);
        }
        // 获取当天 00:00
        long wee = getZeroOfToday();
        if (lastMillisTime >= wee) {
            return String.format("今天%tR", lastMillisTime);
        } else if (lastMillisTime >= wee - UNIT_DAY) {
            return String.format("昨天%tR", lastMillisTime);
        } else {
            return String.format("%tF", lastMillisTime);
        }
    }


    /*获取表示当前日期的0点时间毫秒数.*/
    public static long getZeroOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * Return the day of week in Chinese.
     *
     * @param date The date.
     * @return the day of week in Chinese
     */
    public static String getChineseWeek(final Date date) {
        return new SimpleDateFormat("E", Locale.CHINA).format(date);
    }

    /**
     * Return the day of week in Chinese.
     *
     * @param millisTime The milliseconds.
     * @return the day of week in Chinese
     */
    public static String getChineseWeek(final long millisTime) {
        return getChineseWeek(new Date(millisTime));
    }


    /**
     * Return whether it is am.
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAm(final Date date) {
        return getValueByCalendarField(date, GregorianCalendar.AM_PM) == 0;
    }

    /**
     * Return whether it is am.
     *
     * @param millisTime The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isAm(final long millisTime) {
        return getValueByCalendarField(new Date(millisTime), GregorianCalendar.AM_PM) == 0;
    }


    /**
     * Return whether it is pm.
     *
     * @param date The date.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPm(final Date date) {
        return !isAm(date);
    }

    /**
     * Return whether it is pm.
     *
     * @param millisTime The milliseconds.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPm(final long millisTime) {
        return !isAm(millisTime);
    }

    /**
     * Returns the value of the given calendar field.
     *
     * @param field The given calendar field.
     *              <ul>
     *              <li>{@link Calendar#ERA}</li>
     *              <li>{@link Calendar#YEAR}</li>
     *              <li>{@link Calendar#MONTH}</li>
     *              <li>...</li>
     *              <li>{@link Calendar#DST_OFFSET}</li>
     *              </ul>
     * @return the value of the given calendar field
     */
    public static int getValueByCalendarField(final Date date, final int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * 根据生日计算年龄
     **/
    public static int getAge(String dateStr) {
        return getAge(dateStr, DEFAULT_FORMAT_PATTERN);
    }

    /**
     * 根据生日计算年龄
     **/
    public static int getAge(String dateStr, String pattern) {
        SimpleDateFormat format = getDateFormat(pattern);
        Date birthDay = null;
        try {
            birthDay = format.parse(dateStr);
        } catch (Exception e) {
            return 18;
        }
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            return 18;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 根据日期计算星座
     **/
    public static String getConstellation(long millisTime) {
        return getConstellation(millisTime2Date(millisTime));
    }


    /**
     * 根据生日计算星座
     **/
    public static String getConstellation(String dateStr, String pattern) {
        try {
            DateFormat format = getDateFormat(pattern);
            Date date = format.parse(dateStr);
            return getConstellation(date);
        } catch (Exception e) {
            return "水瓶座";
        }
    }

    /**
     * 根据生日计算星座
     **/
    public static String getConstellation(Date date) {
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String constellation = "";
        if (month == 1 && day >= 20 || month == 2 && day <= 18) {
            constellation = "水瓶座";
        }
        if (month == 2 && day >= 19 || month == 3 && day <= 20) {
            constellation = "双鱼座";
        }
        if (month == 3 && day >= 21 || month == 4 && day <= 19) {
            constellation = "白羊座";
        }
        if (month == 4 && day >= 20 || month == 5 && day <= 20) {
            constellation = "金牛座";
        }
        if (month == 5 && day >= 21 || month == 6 && day <= 21) {
            constellation = "双子座";
        }
        if (month == 6 && day >= 22 || month == 7 && day <= 22) {
            constellation = "巨蟹座";
        }
        if (month == 7 && day >= 23 || month == 8 && day <= 22) {
            constellation = "狮子座";
        }
        if (month == 8 && day >= 23 || month == 9 && day <= 22) {
            constellation = "处女座";
        }
        if (month == 9 && day >= 23 || month == 10 && day <= 23) {
            constellation = "天秤座";
        }
        if (month == 10 && day >= 24 || month == 11 && day <= 22) {
            constellation = "天蝎座";
        }
        if (month == 11 && day >= 23 || month == 12 && day <= 21) {
            constellation = "射手座";
        }
        if (month == 12 && day >= 22 || month == 1 && day <= 19) {
            constellation = "摩羯座";
        }
        return constellation;
    }

    private static final String[] CHINESE_ZODIAC =
            {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};


    /**
     * Return the Chinese zodiac.
     *
     * @param dateStr The formatted time string.
     * @param pattern The format.
     * @return the Chinese zodiac
     */
    public static String getChineseZodiac(final String dateStr, final String pattern) {
        return getChineseZodiac(string2Date(dateStr, pattern));
    }

    /**
     * Return the Chinese zodiac.
     *
     * @param date The date.
     * @return the Chinese zodiac
     */
    public static String getChineseZodiac(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * Return the Chinese zodiac.
     *
     * @param millis The milliseconds.
     * @return the Chinese zodiac
     */
    public static String getChineseZodiac(final long millis) {
        return getChineseZodiac(millisTime2Date(millis));
    }

    /**
     * Return the Chinese zodiac.
     *
     * @param year The year.
     * @return the Chinese zodiac
     */
    public static String getChineseZodiac(final int year) {
        return CHINESE_ZODIAC[year % 12];
    }

    /*得到当日结束时间戳*/
    public static long getEndTime(long curTime) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeInMillis(curTime);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

}
