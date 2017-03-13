package com.stormnology.stadium.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 2/22/2015.
 */
public class DateUtils {

    public static Calendar convertToCalendar(String strDate, String strFormat) {
        try {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            final DateFormat df = new SimpleDateFormat(strFormat, Locale.ENGLISH);
            calendar.setTime(df.parse(strDate));

            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertToString(Calendar calendar, String strFormat) {
        return convertToString(calendar, strFormat, Locale.ENGLISH);
    }

    public static String convertToString(Calendar calendar, String strFormat, Locale locale) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat, locale);
            String strDate = format.format(calendar.getTime());
            return strDate;
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDate(String strDate, String originalFormat, String desiredFormat) {
        return formatDate(strDate, originalFormat, desiredFormat, Locale.ENGLISH);
    }

    public static String formatDate(String strDate, String originalFormat, String desiredFormat, Locale locale) {
        return convertToString(convertToCalendar(strDate, originalFormat), desiredFormat, locale);
    }

    public static String getDayName(String date, String dateFormat) {
        Calendar calendar = convertToCalendar(date, dateFormat);
        return getDayName(calendar);
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

    public static String getNewStringDate(String strDate, String strFormat, int daysToAdd) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        if (calendar != null) {
            calendar.add(Calendar.DATE, daysToAdd);
            return convertToString(calendar, strFormat);
        } else {
            return null;
        }
    }

    public static Calendar addDays(String strDate, String strFormat, int daysToAdd) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        if (calendar != null) {
            calendar.add(Calendar.DATE, daysToAdd);
            return calendar;
        } else {
            return null;
        }
    }

    public static Calendar addDays(int daysToAdd) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.DATE, daysToAdd);
        return calendar;
    }

    public static Calendar addHours(String strDate, String strFormat, int hoursToAdd) {
        Calendar calendar = convertToCalendar(strDate, strFormat);
        calendar.add(Calendar.HOUR, hoursToAdd);
        return calendar;
    }

    /**
     * method, used to compare two dates
     *
     * @param strDate1
     * @param strDate2
     * @param dateFormat
     * @return 1 if date1 is after date2, -1 if date1 is before date2 and 0 if the the dates are the same
     */
    public static int compare(String strDate1, String strDate2, String dateFormat) {
        Calendar calendar1 = convertToCalendar(strDate1, dateFormat);
        Calendar calendar2 = convertToCalendar(strDate2, dateFormat);

        return calendar1.compareTo(calendar2);
    }

    public static long difference(String strDate1, String strDate2, String dateFormat) {
        long calMillis1 = convertToCalendar(strDate1, dateFormat).getTimeInMillis();
        long calMillis2 = convertToCalendar(strDate2, dateFormat).getTimeInMillis();

        return calMillis2 - calMillis1;
    }

    public static String getLocalizedDate(String date) {
        try {
            // check language
            String lang = Utils.getAppLanguage();
            if ("ar".equals(lang)) {
                // arabic
                // format date to be arabic
                date = date.toLowerCase().replace("am", "ص").replace("pm", "م");
            } else {
                // not arabic,
                // mostly it should be english
                // format ∂ate to be english
                date = date.replace("ص", "AM").replace("م", "PM");
            }

        } catch (Exception e) {
        }

        return date;
    }
}
