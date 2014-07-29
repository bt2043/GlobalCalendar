package org.henyue.globalcalendar.service;

import org.henyue.globalcalendar.service.impl.CalendarServiceImpl;

/**
 * Created by henyue on 2014/7/30.
 */
public class ServiceFactory {
    private static CalendarService calendarService = new CalendarServiceImpl();

    public static CalendarService getCalendarService() {
        return calendarService;
    }
}
