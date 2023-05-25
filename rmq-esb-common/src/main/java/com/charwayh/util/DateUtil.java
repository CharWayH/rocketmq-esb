package com.charwayh.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 日期操作辅助类
 *
 * @author ShenHuaJie
 * @version $Id: DateUtil.java, v 0.1 2014年3月28日 上午8:58:11 ShenHuaJie Exp $
 */
@Slf4j
public final class DateUtil {
    private DateUtil() {
    }


    /**
     * 日期格式
     **/
    public interface DATE_PATTERN {
        String HHMMSS                  = "HHmmss";
        String HH_MM_SS                = "HH:mm:ss";
        String YYYY                    = "yyyy";
        String YYYYMM                  = "yyyy.MM";
        String YYYYMMDD                = "yyyyMMdd";
        String YYYY_MM_DD              = "yyyy-MM-dd";
        String YYYYMMDDHH              = "yyyy-MM-dd HH";
        String YYYYMMDDHHMMSS          = "yyyyMMddHHmmss";
        String YYYYMMDDHHMMSSSSS       = "yyyyMMddHHmmssSSS";
        String YYYY_MM_DD_HH_MM_SS     = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
        String YYYYMMDD_HHMMSSSSS      = "yyyyMMdd HHmmssSSS";
    }

    public interface DateRange {
        String START = "START";
        String END   = "END";
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static final String format(Object date) {
        return format(date, DATE_PATTERN.YYYY_MM_DD);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Object date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    public static final String formatToZ8(Object date, String pattern) {
        String suffix = " +0800";
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date) + suffix;
        }
        return new SimpleDateFormat(pattern).format(date) + suffix;
    }


    /**
     * 获取日期
     *
     * @return
     */
    public static final String getDate() {
        return format(new Date());
    }


    /**
     * 获取日期时间
     *
     * @return
     */
    public static final String getDateTime() {
        return format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取日期
     *
     * @param pattern
     * @return
     */
    public static final String getDateTime(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 日期计算
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static final Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 字符串转换为日期:不支持yyM[M]d[d]格式
     *
     * @param date
     * @return
     */
    public static final Date stringToDate(String date) {
        if (date == null) {
            return null;
        }
        String separator = String.valueOf(date.charAt(4));
        String pattern   = "yyyyMMdd";
        if (!separator.matches("\\d*")) {
            pattern = "yyyy" + separator + "MM" + separator + "dd";
            if (date.length() < 10) {
                pattern = "yyyy" + separator + "M" + separator + "d";
            }
            pattern += " HH:mm:ss.SSS";
        } else if (date.length() < 8) {
            pattern = "yyyyMd";
        } else {
            pattern += "HHmmss.SSS";
        }
        pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final double getDayBetween(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
//        start.set(Calendar.HOUR_OF_DAY, 0);
//        start.set(Calendar.MINUTE, 0);
//        start.set(Calendar.SECOND, 0);
//        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
//        end.set(Calendar.HOUR_OF_DAY, 0);
//        end.set(Calendar.MINUTE, 0);
//        end.set(Calendar.SECOND, 0);
//        end.set(Calendar.MILLISECOND, 0);

        long   n     = end.getTimeInMillis() - start.getTimeInMillis();
        double value = n / (60 * 60 * 24 * 1000.0);
        return value;
    }

    /**
     * 间隔月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1  = start.get(Calendar.YEAR);
        int year2  = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n      = (year2 - year1) * 12;
        n = n + month2 - month1;
        return n;
    }

    /**
     * 间隔月，多一天就多算一个月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static final Integer getMonthBetweenWithDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || !startDate.before(endDate)) {
            return null;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year1  = start.get(Calendar.YEAR);
        int year2  = end.get(Calendar.YEAR);
        int month1 = start.get(Calendar.MONTH);
        int month2 = end.get(Calendar.MONTH);
        int n      = (year2 - year1) * 12;
        n = n + month2 - month1;
        int day1 = start.get(Calendar.DAY_OF_MONTH);
        int day2 = end.get(Calendar.DAY_OF_MONTH);
        if (day1 <= day2) {
            n++;
        }
        return n;
    }

    /**
     * 根据传入字符串时间返回相对应字符串格式 2014年11月17日 hb
     *
     * @param date
     * @param parseP
     * @param formatP
     * @return
     */
    public static String getDateStr(String date, String parseP, String formatP) {
        SimpleDateFormat sdf_parse  = new SimpleDateFormat(parseP);
        SimpleDateFormat sdf_format = new SimpleDateFormat(formatP);
        if (StringUtils.isEmpty(date)) {
            return null;
        }
        try {
            return sdf_format.format(sdf_parse.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入字符串时间返回相对应时间格式 2014年11月17日 hb
     *
     * @param date
     * @param format
     * @return
     */
    public static Date getDate(String date, String format) {
        SimpleDateFormat simp = new SimpleDateFormat(format);
        try {
            return simp.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入字符串时间返回相对应时间格式 2014年11月17日 hb
     *
     * @param date
     * @param parseP
     * @param formatP
     * @return
     */
    public static Date getDate(Date date, String parseP, String formatP) {
        SimpleDateFormat sdf_parse  = new SimpleDateFormat(parseP);
        SimpleDateFormat sdf_format = new SimpleDateFormat(formatP);
        if (date == null) {
            return null;
        }
        try {
            return sdf_format.parse(sdf_parse.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据时间。拿到一个 int 的时、或者分、或者秒
     * 参数为 C
     *
     * @param date
     * @param type 为Calendar 类中时分秒类型对应的int值
     * @return
     */
    public static int getIntField(Date date, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(type);
    }

    /**
     * @Author SangYD
     * @Description 获得某天最大时间 2017-10-15 23:59:59
     * @Date 14:59 2018/10/15
     * @Param [data] 待获取的日期
     * @Return java.cc.ewell.sdk.util.Date
     **/
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay      = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @Author SangYD
     * @Description 获得某天最小时间 2017-10-15 00:00:00
     * @Date 14:59 2018/10/15
     * @Param [data] 待获取的日期
     * @Return java.cc.ewell.sdk.util.Date
     **/
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay    = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getStartOfHour(Date date) {
        Date             returnDate = date;
        SimpleDateFormat oldSdf     = new SimpleDateFormat(DATE_PATTERN.YYYYMMDDHH);
        SimpleDateFormat newSdf     = new SimpleDateFormat(DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
        String           dateStr    = oldSdf.format(date) + ":00:00";
        try {
            returnDate = newSdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static Date getEndOfHour(Date date) {
        Date             returnDate = date;
        SimpleDateFormat oldSdf     = new SimpleDateFormat(DATE_PATTERN.YYYYMMDDHH);
        SimpleDateFormat newSdf     = new SimpleDateFormat(DATE_PATTERN.YYYY_MM_DD_HH_MM_SS);
        String           dateStr    = oldSdf.format(date) + ":59:59";
        try {
            returnDate = newSdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static String convert(String dateStr, String oldPattern, String newPattern) {
        String retVal = dateStr;
        try {
            SimpleDateFormat oldSdf = new SimpleDateFormat(oldPattern);
            Date             date   = oldSdf.parse(dateStr);

            SimpleDateFormat newSdf = new SimpleDateFormat(newPattern);
            retVal = newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static List<String> getDaysBetween(Date startTime, Date endTime) {
        List<String>     dateStrs = new ArrayList<>();
        SimpleDateFormat format   = new SimpleDateFormat(DATE_PATTERN.YYYY_MM_DD);
        long             start    = startTime.getTime();
        long             end      = endTime.getTime();
        long             dateLong = 24 * 60 * 60 * 1000L;
        do {
            String day = format.format(new Date(start));
            dateStrs.add(day);
            start += dateLong;
        } while (start < end);
        return dateStrs;
    }


    public static List<String> getDaysBetween(Date startTime, Date endTime, boolean include) {
        if (include) {
            List<String>     dateStrs = new ArrayList<>();
            SimpleDateFormat format   = new SimpleDateFormat(DATE_PATTERN.YYYY_MM_DD);
            long             start    = startTime.getTime();
            long             end      = endTime.getTime();
            long             dateLong = 24 * 60 * 60 * 1000L;
            if (start > end) {
                return dateStrs;
            } else {
                String endStr   = format.format(new Date(end));
                String startStr = null;
                do {
                    startStr = format.format(new Date(start));
                    dateStrs.add(startStr);
                    start += dateLong;
                } while (!endStr.equalsIgnoreCase(startStr));
                return dateStrs;
            }
        } else {
            return getDaysBetween(startTime, endTime);
        }
    }


    public static List<String> getDaysBetween(long start, long end) {
        Date startTime = new Date(start);
        Date endTime   = new Date(end);
        return getDaysBetween(startTime, endTime);
    }

    /**
     * @Author WangDL
     * @Description 获取近N天日期的开始和结束时间
     * @Date 11:32 2019/5/5
     * @Param [now, daysToMinius]
     * @Return java.cc.ewell.sdk.util.List<java.lang.String>
     **/
    public static Map<String, Date> getNearDays(Date now, int daysBetween) {
        Map<String, Date> map      = new HashMap<String, Date>();
        String            startKey = DateRange.START;
        String            endKey   = DateRange.END;
        if (daysBetween == 0) {
            //当天
            map.put(startKey, getStartOfDay(now));
            map.put(endKey, getEndOfDay(now));
        } else if (daysBetween < 0) {
            //近N天
            Calendar start = Calendar.getInstance();
            start.setTime(now);
            start.add(Calendar.DATE, daysBetween);
            start.set(Calendar.MILLISECOND, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.HOUR_OF_DAY, 0);
            map.put(startKey, start.getTime());

            Calendar end = Calendar.getInstance();
            end.setTime(now);
            end.set(Calendar.MILLISECOND, 0);
            end.set(Calendar.SECOND, 0);
            end.set(Calendar.MINUTE, 0);
            end.set(Calendar.HOUR_OF_DAY, 0);
            end.add(Calendar.MILLISECOND, -1);
            map.put(endKey, end.getTime());
        } else {
            throw new RuntimeException("daysBetween 参数不合法,必须是小于或者等于0的值");
        }
        return map;
    }


    /**
     * 获取日期差值: 如 2d12h32m
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getDaysDiff(Date startTime, Date endTime) {
        if (startTime == null && endTime == null) {
            throw new NullPointerException("开始时间或者结束时间为空");
        }
        long start = startTime.getTime();
        long end   = endTime.getTime();
        long diff  = end - start;
        if (diff < 0) {
            throw new IllegalArgumentException("开始时间大于结束时间");
        }

        long day    = 24 * 60 * 60 * 1000L;
        long hour   = 60 * 60 * 1000L;
        long minute = 60 * 1000L;
        long second = 1000L;

        StringBuilder sb = new StringBuilder();
        long          d  = diff / day;
        sb.append(d).append("d");
        diff = (diff % day);

        long h = diff / hour;
        sb.append(h).append("h");
        diff = (diff % hour);

        long m = diff / minute;
        sb.append(m).append("m");
        diff = (diff % minute);

//        long s = diff / second;
//        sb.append(s).append("s");
//        diff = (diff % second);
        return sb.toString();
    }
}
