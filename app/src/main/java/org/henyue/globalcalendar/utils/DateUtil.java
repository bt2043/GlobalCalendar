package org.henyue.globalcalendar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by henyue on 2014/7/31.
 */
public class DateUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Date getCleanDate(Calendar calendar) {
        if (calendar != null) {
            try {
                return sdf.parse(sdf.format(calendar.getTime()));
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}
