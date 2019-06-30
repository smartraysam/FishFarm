package com.smartdev.fishfarm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    private static final Long aDay = Long.valueOf(86400000);
    private static final Long anHr = Long.valueOf(3600000);

    public static long datetime_to_millis(String date, String time) {
        long millis;
        StringBuilder date_time = new StringBuilder();
        date_time.append(date);
        date_time.append(" ");
        date_time.append(time);
        try {
            millis = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(date_time.toString()).getTime();
        } catch (ParseException e) {
            millis = 0;
        }
        return millis;
    }

    public static long time_to_millis(String val, String type) {
        if (type.equals("mm")) {
            return TimeUnit.MINUTES.toMillis(Long.valueOf(val).longValue());
        }
        if (type.equals("hh")) {
            return TimeUnit.HOURS.toMillis(Long.valueOf(val).longValue());
        }
        if (type.equals("dd")) {
            return TimeUnit.DAYS.toMillis(Long.valueOf(val).longValue());
        }
        return 0;
    }

    public static String getDate(long millis) {
        return String.format(Locale.ENGLISH, "%02d day(s) %02dh %02dmin", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toDays(millis)), Long.valueOf(TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))});
    }

    public static String getHrMin(long millis) {
        return String.format(Locale.ENGLISH, "%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(millis)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))});
    }

    public static String getDuration(long millis) {
        String result = "";
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        } else if (millis >= 86400000) {
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
            return String.format(Locale.ENGLISH, "%02d day(s) %02dh", new Object[]{Long.valueOf(days), Long.valueOf(hours)});
        } else if (millis >= 86400000 || millis < 3600000) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
            return String.format(Locale.ENGLISH, "%02dmin", new Object[]{Long.valueOf(minutes)});
        } else {
           long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
            long minutes2 = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
            return String.format(Locale.ENGLISH, "%02dh %02dmin", new Object[]{Long.valueOf(hours), Long.valueOf(minutes2)});
        }
    }

    public static int getEventFilter(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        } else if (millis >= anHr && millis < anHr * 2) {
            return 1;
        } else {
            if (millis >= anHr * 2 && millis < anHr * 4) {
                return 2;
            }
            if (millis >= anHr * 4 && millis < anHr * 6) {
                return 3;
            }
            if (millis >= anHr * 6 && millis < anHr * 12) {
                return 4;
            }
            if (millis >= anHr * 12 && millis < anHr * 24) {
                return 5;
            }
            if (millis >= aDay && millis < aDay * 7) {
                return 6;
            }
            int i = 0;
            int i2 = millis >= aDay * 7 ? 1 : 0;
            if (millis < aDay * 30) {
                i = 1;
            }
            if ((i2 & i) != 0) {
                return 7;
            }
            if (millis >= aDay * 30) {
                return 8;
            }
            return 0;
        }
    }
}
