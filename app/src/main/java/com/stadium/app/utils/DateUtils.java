package com.stadium.app.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 2/22/2015.
 */
public class DateUtils {
    public static Calendar convertToCalendar(String strDate, String strFormat) {
        Calendar calendar = Calendar.getInstance();
        try {
            final DateFormat df = new SimpleDateFormat(strFormat);
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String convertToString(Calendar calendar, String strFormat) {
        String strDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            strDate = format.format(calendar.getTime());
        } catch (Exception e) {
            strDate = null;
            e.printStackTrace();
        }

        return strDate;
    }

    public static String formatDate(String strDate, String originalFormat, String desiredFormat) {
        return convertToString(convertToCalendar(strDate, originalFormat), desiredFormat);
    }

    public static String getDayName(String date, String dateFormat) {
        Calendar calendar = convertToCalendar(date, dateFormat);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayName = sdf.format(calendar.getTime());

        return dayName;
    }

    public static String getDayName(Calendar date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayName = sdf.format(date.getTime());

        return dayName;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        return convertToString(calendar, "hh:mm:ss");
    }

    public static boolean isCurrentDate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return (calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH)
                && calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH))
                && calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR);
    }

    public static boolean isPastDate(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return calendar.getTimeInMillis() < currentCalendar.getTimeInMillis();
    }

    public static boolean isToday(Calendar calendar) {
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        return currentCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String addDays(String strDate, String strFormat, int days) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        if (calendar != null) {
            calendar.add(Calendar.DATE, days);
            return convertToString(calendar, strFormat);
        } else {
            return null;
        }
    }
}
