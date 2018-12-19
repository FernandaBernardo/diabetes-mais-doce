package br.com.diabetesmaisdoce.extras;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Fernanda Bernardo on 18/04/2016.
 */
public class ParserTools {
    public static String getParseDouble(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        return decimalFormat.format(value);
    }
    public static String getParseDate(Calendar date) {
        return getParseNumber(date.get(Calendar.DAY_OF_MONTH)) + "/"
                + getParseNumber(date.get(Calendar.MONTH) + 1) + "/"
                + getParseNumber(date.get(Calendar.YEAR));
    }

    public static String getParseDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
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
