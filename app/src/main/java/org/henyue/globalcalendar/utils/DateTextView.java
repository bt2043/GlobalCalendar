package org.henyue.globalcalendar.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.henyue.globalcalendar.ActivityMonthCalendar;
import org.henyue.globalcalendar.R;
import org.henyue.globalcalendar.beans.event.DayEvent;
import org.henyue.globalcalendar.service.ServiceFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by henyue on 2014/7/22.
 */
public class DateTextView extends TextView {
    private Context context;
    private ActivityMonthCalendar monthCalendar;
    private Calendar current = Calendar.getInstance();
    private Date date = null;

    public DateTextView(Context context, ActivityMonthCalendar monthCalendar) {
        super(context);
        this.context = context;
        this.monthCalendar = monthCalendar;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Calendar cal) {
        final Date date = cal.getTime();
        this.date = DateUtil.getCleanDate(cal);
        this.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        if (cal.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
                && cal.get(Calendar.YEAR) == current.get(Calendar.YEAR)) {
            this.setBackgroundResource(R.drawable.bg_selected_date);
        }
        final List<DayEvent> dayEventList = ServiceFactory.getCalendarService().getDayEventList(date);
        if (!dayEventList.isEmpty()) {
            for (int i = 0; i < dayEventList.size(); i++) {
                DayEvent event = dayEventList.get(i);
                ViewBadger badger = new ViewBadger(context, this);
                badger.setHeight(12);
                badger.setWidth(12);
                //TODO define the festival color
                badger.setSolidColor(event.getRegion().color);
                badger.setBadgePosition(ViewBadger.POSITION_BOTTOM_CENTER);
                badger.setBadgeMargin(15*i, 0);
                badger.show();
            }
        }
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                monthCalendar.resetOnTouchView(DateTextView.this.getDate());
                String msg = "The date you selected is " + DateTextView.this.getDate();
                for (DayEvent event : dayEventList) {
                    msg += "\n" + event.getName() + " - [" + event.getDesc() + "]";
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*Paint paint = new Paint();
        //paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.GRAY);
        //paint.setStyle(Paint.Style.FILL);
        int[] positions = new int[2];
        this.getLocationInWindow(positions);
        float positionX = positions[0];
        float positionY = positions[1];
        float circleX = this.getWidth()/2 + positionX;
        float circleY = this.getHeight()/2 + positionY;
        int length = (this.getWidth() > this.getHeight() ? this.getHeight() : this.getWidth())/2 - 1;
        //Draw border
        //canvas.drawCircle(circleX, circleY, length, paint);
        //canvas.drawColor(Color.RED);
        //canvas.drawRect(positionX, positionY, circleX, circleY, paint);
        canvas.drawCircle(circleX, circleY, 20, paint);
        System.err.println("circleX:" + circleX + ", circleY:" + circleY);*/

    }


}
