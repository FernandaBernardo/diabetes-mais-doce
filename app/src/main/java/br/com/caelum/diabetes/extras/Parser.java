package br.com.caelum.diabetes.extras;

/**
 * Created by Fernanda Bernardo on 18/04/2016.
 */
public class Parser {
    public static String getParseDate(int day, int month, int year) {
        return getParseNumber(day) + "/" + getParseNumber(month + 1) + "/" + getParseNumber(year);
    }

    public static String getParseHour(int hour, int minute) {
        return getParseNumber(hour) + ":" + getParseNumber(minute);
    }

    private static String getParseNumber(int number) {
        if (number < 10) return "0" + number;
        return number + "";
    }
}
