package com.headyassignment.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    public static final String TEMPLATE_STANDARD_DATE_AND_TIME_TIMEZONE_MILLI = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    @NonNull
    public static Date parseStringDate(@NonNull String strDate, String pattern) {
        SimpleDateFormat simpleDateFormatReverse = new SimpleDateFormat(pattern,
                Locale.getDefault());
        Date date1 = null;
        try {
            date1 = simpleDateFormatReverse.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String getStandardDate(Date date, String returnTypeFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(returnTypeFormat, Locale
                .getDefault());
        return simpleDateFormat.format(date);
    }
}
