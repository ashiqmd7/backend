package com.G2T5203.wingit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static Date parseDateTime(String datetimeString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
        return dateFormat.parse(datetimeString);
    }

    public static Date handledParseDateTime(String datetimeString) {
        try {
            return parseDateTime(datetimeString);
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        return dateFormat.parse(dateString);
    }

    public static Date handledParseDate(String dateString) {
        try {
            return parseDate(dateString);
        } catch (ParseException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        String datetimeString = "2023-09-15 14:30:00";

        try {
            Date date = parseDateTime(datetimeString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
