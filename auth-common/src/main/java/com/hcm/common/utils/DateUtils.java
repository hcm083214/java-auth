package com.hcm.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式：年月天
     */
    public static final String YYYYMMdd = "YYYYMMdd";

    /**
     * 时间格式：年月天小时
     */
    public static final String YYYYMMddHH = "YYYYMMddHH";

    /**
     * 时间格式：年月天小时分钟
     */
    public static final String YYYYMMddHHmm = "YYYYMMddHHmm";

    private static SimpleDateFormat FORMAT_YYYYMMdd = new SimpleDateFormat(YYYYMMdd);
    private static SimpleDateFormat FORMAT_YYYYMMddHH = new SimpleDateFormat(YYYYMMddHH);
    private static SimpleDateFormat FORMAT_YYYYMMddHHmm = new SimpleDateFormat(YYYYMMddHHmm);


    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获得当前天往前 during 天的时间列表
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link String}>
     */
    public static List<String> getDuringList(Date startTime, Date endTime) {

        List<String> duringList = new ArrayList<>();
        long delta = endTime.getTime() - startTime.getTime();
        if (startTime.compareTo(endTime) == 0 || delta == 0) {
            return duringList;
        }
        Date temp = startTime;
        if (delta < MILLIS_PER_HOUR) {
            // 时间差小于 1 小时
            int minutesDiff = (int) (delta / MILLIS_PER_MINUTE);
            while (endTime.compareTo(temp) >= 0 && minutesDiff > 0) {
                duringList.add(FORMAT_YYYYMMddHHmm.format(temp));
                temp = addMinutes(temp, 1);
            }
        } else if (delta < MILLIS_PER_DAY) {
            // 时间差小于 1 天
            int hoursDiff = (int) (delta / MILLIS_PER_HOUR);
            while (endTime.compareTo(temp) >= 0 && hoursDiff > 0) {
                duringList.add(FORMAT_YYYYMMddHH.format(temp));
                temp = addHours(temp, 1);
            }
        } else {
            // 时间差大于 1 天
            int daysDiff = (int) (delta / MILLIS_PER_DAY);
            while (endTime.compareTo(temp) >= 0 && daysDiff > 0) {
                duringList.add(FORMAT_YYYYMMdd.format(temp));
                temp = addDays(temp, 1);
            }
        }

        return duringList;
    }

    /**
     * 获取从 date 往前推 minutes 分钟的键列表
     *
     * @param date    特定日期
     * @param minutes 分钟数
     * @return 键列表
     */
    public static List<String> getLastMinutes(Date date, int minutes) {
        return getDuringList(DateUtils.addMinutes(date, -1 * minutes), date);
    }

    /**
     * 获取从 date 往前推 hours 个小时的键列表
     *
     * @param date  特定日期
     * @param hours 小时数
     * @return 键列表
     */
    public static List<String> getLastHours(Date date, int hours) {
        return getDuringList(DateUtils.addHours(date, -1 * hours), date);
    }

    /**
     * 获取从 date 开始往前推 days 天的键列表
     *
     * @param date 特定日期
     * @param days 天数
     * @return 键列表
     */
    public static List<String> getLastDays(Date date, int days) {
        return getDuringList(DateUtils.addDays(date, -1 * days), date);
    }

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
}
