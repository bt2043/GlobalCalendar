package org.henyue.globalcalendar.service;

import org.henyue.globalcalendar.beans.event.DayEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by henyue on 2014/7/30.
 */
public interface CalendarService {
    public List<DayEvent> getDayEventList(Date date);
}
