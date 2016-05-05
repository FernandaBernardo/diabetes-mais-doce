package br.com.caelum.diabetes.extras;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Fernanda Bernardo on 18/04/2016.
 */
public class ParserTools {
    public static String getParseDate(Calendar date) {
        return getParseNumber(date.get(Calendar.DAY_OF_MONTH)) + "/"
                + getParseNumber(Calendar.MONTH + 1) + "/"
                + getParseNumber(Calendar.YEAR);
    }

    public static String getParseDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return getParseDate(calendar);
    }

    public static String getParseHour(Calendar hour) {
        return getParseNumber(hour.get(Calendar.HOUR_OF_DAY)) + ":"
                + getParseNumber(hour.get(Calendar.MINUTE));
    }

    public static String getParseHour(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return getParseHour(calendar);
    }

    private static String getParseNumber(int number) {
        if (number < 10) return "0" + number;
        return number + "";
    }
}
