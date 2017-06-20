package com.bobao.identifypro.utils;

import android.text.TextUtils;

import com.bobao.identifypro.constant.IntentConstant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }

    /**
     * 将字符串转成时间戳，取10位
     */
    public static String timeStrToTimestampStr(String time, String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        try {
            Date date = sdf.parse(time);
            return StringUtils.getString(date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间戳转成相应格式的时间字符串
     */
    public static String timestampStrToFormatTime(String timestamp, String timeFormat) {
        if (TextUtils.isEmpty(timestamp)) {
            return "";
        }
        if (TextUtils.isEmpty(timeFormat)) timeFormat = IntentConstant.SELECT_A_DATE_TIME_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(new Date(Long.valueOf(timestamp)));
    }

    /**
     * 两个时间是不是相隔在一天之内
     *
     * @param currentTime
     * @param preTime
     * @return
     */
    public static boolean isOneDayApartOfMillis(String currentTime, String preTime) {
        if (TextUtils.isEmpty(currentTime)) {
            currentTime = "0";
        }
        if (TextUtils.isEmpty(preTime)) {
            preTime = "0";
        }
        long currentTimeLong = Long.valueOf(currentTime);
        long preTimeLong = Long.valueOf(preTime);
        final long interval = currentTimeLong - preTimeLong;
        return interval < MILLIS_IN_DAY;
    }
}