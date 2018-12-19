package br.com.diabetesmaisdoce.extras;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateTimeType;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Fernanda Bernardo on 04/05/2016.
 */
public class CalendarTypePersister extends DateTimeType {
    private static CalendarTypePersister singleton;

    protected CalendarTypePersister() {
        super(SqlType.LONG, new Class<?>[0]);
    }

    public static CalendarTypePersister getSingleton() {
        if (singleton == null) {
            singleton = new CalendarTypePersister();
        }
        return singleton;
    }

    @Override
    public boolean isValidForField(Field field) {
        return Calendar.class.isAssignableFrom(field.getType());
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object obj) throws SQLException {
        Calendar calendar = (Calendar) obj;
        if (calendar == null) {
            return null;
        } else {
            return calendar.getTimeInMillis();
        }
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date((Long) sqlArg));
        return calendar;
    }
}
