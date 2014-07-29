package org.henyue.globalcalendar.service.impl;

import org.henyue.globalcalendar.beans.event.DayEvent;
import org.henyue.globalcalendar.beans.event.Region;
import org.henyue.globalcalendar.service.CalendarService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by henyue on 2014/7/30.
 */
public class CalendarServiceImpl implements CalendarService{
    @Override
    public List<DayEvent> getDayEventList(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        List<DayEvent> resultList = new ArrayList<DayEvent>();
        if (cal.get(Calendar.MONTH) + 1 == 7 && cal.get(Calendar.DAY_OF_MONTH) == 1) {
            resultList.add(new DayEvent("建党节", "中国共产党成立日", Region.CHINA));
        } else if (cal.get(Calendar.MONTH) + 1 == 7 && cal.get(Calendar.DAY_OF_MONTH) == 4) {
            resultList.add(new DayEvent("Independence Day", "Independence Day", Region.US));
        } else if (cal.get(Calendar.MONTH) + 1 == 7 && cal.get(Calendar.DAY_OF_MONTH) == 12) {
            resultList.add(new DayEvent("Orangemen's Day", "Orangemen's Day", Region.UK));
        } else if (cal.get(Calendar.MONTH) + 1 == 7 && cal.get(Calendar.DAY_OF_MONTH) == 14) {
            resultList.add(new DayEvent("Orangemen's Day observed", "Orangemen's Day observed", Region.UK));
        } else if (cal.get(Calendar.MONTH) + 1 == 7 && cal.get(Calendar.DAY_OF_MONTH) == 25) {
            resultList.add(new DayEvent("Jamat Ul-Vida", "Jamat Ul-Vida", Region.INDIA));
            resultList.add(new DayEvent("随便", "老子想过节", Region.CHINA));
            resultList.add(new DayEvent("SO", "阿三过节，主子也要过", Region.UK));
        } else if (cal.get(Calendar.MONTH) + 1 == 7 && cal.get(Calendar.DAY_OF_MONTH) == 30) {
            resultList.add(new DayEvent("Ramzan Id/Eid-ul-Fitar", "Ramzan Id/Eid-ul-Fitar", Region.INDIA));
            resultList.add(new DayEvent("随便", "老子想过节", Region.CHINA));
        } else if (cal.get(Calendar.MONTH) + 1 == 8 && cal.get(Calendar.DAY_OF_MONTH) == 1) {
            resultList.add(new DayEvent("建军节", "中国人民解放军成立日", Region.CHINA));
        } else if (cal.get(Calendar.MONTH) + 1 == 8 && cal.get(Calendar.DAY_OF_MONTH) == 2) {
            resultList.add(new DayEvent("七夕", "中国传统节日，传说中牛郎织女鹊桥相会的日子，又称中国情人节", Region.CHINA));
        } else if (cal.get(Calendar.MONTH) + 1 == 8 && cal.get(Calendar.DAY_OF_MONTH) == 15) {
            resultList.add(new DayEvent("中元节", "中国传统节日，又称中秋节，习俗吃月饼，庆团圆", Region.CHINA));
        }
        return resultList;
    }
}
