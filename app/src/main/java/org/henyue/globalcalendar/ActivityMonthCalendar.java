package org.henyue.globalcalendar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.henyue.globalcalendar.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityMonthCalendar extends Activity {

    private TableLayout calendarLayout;
    private TextView monthNumber;
    private int dateColLen;
    private Calendar calendar;
    private Date preSelectedDate;
    private Date selectedDate;
    private Map<Date, DateTextView> dateViewMap;

    private GestureDetector calendarGesture;
    private View.OnTouchListener onTouchListener;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int COLOR_SELECTED_DATE = Color.RED;
    private static final int COLOR_NORMAL_DATE = Color.parseColor("#388A13");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.onTouchListener = new CalendarTouchListener();
        this.calendarGesture = new GestureDetector(this,new GestureListener());
        this.calendar = Calendar.getInstance();
        this.monthNumber = new TextView(this);
        this.calendarLayout = new TableLayout(this);
        this.dateViewMap = new HashMap<Date, DateTextView>();
        calendarLayout.setAlpha(0.9f);
        //TODO set main calendar's background resource
        //calendarLayout.setBackgroundResource(R.drawable.bg_month_view_calendar_bmp);

        LinearLayout linearLayout = this.initBasicActivity();
        linearLayout.setOnTouchListener(this.onTouchListener);
        linearLayout.setLongClickable(true);
        super.setContentView(linearLayout);
    }

    private LinearLayout initBasicActivity() {
        LinearLayout linearLayout = this.createMatchParentLinearLayout(LinearLayout.VERTICAL, Gravity.TOP);
        linearLayout.setBackgroundResource(R.drawable.bg_month_view);

        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;     // 屏幕宽度（像素）
        this.dateColLen = screenWidth/8;         //按屏幕宽度均分8等份，7份为日历周日到周六，1份宽度为两边留白

        LinearLayout weekHeader = this.initWeekHeader();
        FrameLayout mainCalendar = this.initialMonthCalendar(this.calendar);

        //Add weekHeader and mainCalendar to mainActivity
        linearLayout.addView(weekHeader);
        linearLayout.addView(mainCalendar);
        return linearLayout;
    }

    private LinearLayout initWeekHeader() {
        TableLayout tableLayout = new TableLayout(this);
        TableRow weekRow = new TableRow(this);
        weekRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        String[] weeks = super.getResources().getStringArray(R.array.week);
        for (String week : weeks) {
            TextView weekLabel = new TextView(this);
            weekLabel.setText(week);
            //TODO reset the textColor to #388A13
            weekLabel.setTextColor(Color.parseColor("#FFFFFF"));
            weekLabel.setGravity(Gravity.CENTER);
            weekLabel.setWidth(dateColLen);
            weekRow.addView(weekLabel, new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
        }
        tableLayout.addView(weekRow, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout weekRowContainer = this.createMatchParentLinearLayout(LinearLayout.HORIZONTAL, Gravity.CENTER_VERTICAL);
        //TODO set weekRow's background resource and remove the background color setting
        weekRowContainer.setBackgroundColor(Color.BLACK);
        //weekRowContainer.setBackgroundResource(R.drawable.bg_month_view_week_bmp);
        weekRowContainer.addView(tableLayout);
        return weekRowContainer;
    }

    private FrameLayout initialMonthCalendar(Calendar calendar) {
        this.refreshCalendar(calendar);

        FrameLayout calendarContainer = new FrameLayout(this);
        calendarContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));
        //Background Month Text
        monthNumber.setTextSize(180f);
        monthNumber.setTextColor(Color.WHITE);
        monthNumber.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        monthNumber.setAlpha(0.9f);
        monthNumber.setTypeface(Typeface.MONOSPACE);
        monthNumber.setGravity(Gravity.CENTER_HORIZONTAL);
        //TODO to be removed after set main calendar's background
        calendarContainer.setBackgroundColor(Color.parseColor("#BBFFFFFF"));
        calendarContainer.addView(monthNumber);
        calendarContainer.addView(calendarLayout);
        return calendarContainer;
    }

    private void refreshCalendar(Calendar calendar) {
        this.dateViewMap.clear();
        monthNumber.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        calendarLayout.removeAllViews();
        DateTextView[][] dateViews = new DateTextView[6][7];
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        boolean isEndOfMonth = false;

        for (int i = 0; i < dateViews.length; i++) {
            if (isEndOfMonth) {
                this.resetCalendarTextColor();
                return;
            }
            TableRow dateRow = new TableRow(this);
            dateRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            for (int j = 0; j < dateViews[i].length; j++) {

                dateViews[i][j] = new DateTextView(this);
                dateViews[i][j].setGravity(Gravity.CENTER);
                dateViews[i][j].setWidth(dateColLen);
                dateViews[i][j].setHeight(dateColLen);

                dateRow.addView(dateViews[i][j], new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                ));

                if (isEndOfMonth) {
                    continue;
                }

                if (j >= dayOfWeek - 1) {
                    dateViews[i][j].setDate(calendar);
                    dateViews[i][j].setTextColor(COLOR_NORMAL_DATE);
                    dateViews[i][j].setTypeface(Typeface.MONOSPACE);
                    dateViews[i][j].setTextSize(22f);
                    this.dateViewMap.put(dateViews[i][j].getDate(), dateViews[i][j]);

                    if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        isEndOfMonth = true;
                    }
                    calendar.roll(Calendar.DAY_OF_MONTH, true);
                }
            }
            dayOfWeek = 1;   //every row should start from sunday except the first week row
            calendarLayout.addView(dateRow, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_month_calendar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private LinearLayout createMatchParentLinearLayout(int orientation, int verticalGravity) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(orientation);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        container.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        container.setVerticalGravity(verticalGravity);
        return container;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {

                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    nextMonth();
                    return true; // Right to left
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    previousMonth();
                    return true; // Left to right
                }

            return false;
        }
    }

    private class CalendarTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return calendarGesture.onTouchEvent(motionEvent);
        }
    }

    public void resetOnTouchView(Date selectedDate) {
        this.preSelectedDate = this.selectedDate;
        this.selectedDate = selectedDate;
        this.resetCalendarTextColor();
    }

    private void resetCalendarTextColor() {
        if (this.selectedDate == null && this.preSelectedDate == null) {
            this.selectedDate = DateUtil.getCleanDate(Calendar.getInstance());
        }

        DateTextView dateView;
        if (this.selectedDate != null) {
            if (this.selectedDate.equals(this.preSelectedDate)) {
                return; //反复点击同个日期不处理
            }
            dateView = dateViewMap.get(this.selectedDate);
            if (dateView != null) dateView.setTextColor(COLOR_SELECTED_DATE);
        }
        if (this.preSelectedDate != null) {
            dateView = dateViewMap.get(this.preSelectedDate);
            if (dateView != null) dateView.setTextColor(COLOR_NORMAL_DATE);
        }
    }

    private void nextMonth() {
        this.calendar.roll(Calendar.MONTH, true);
        this.refreshCalendar(this.calendar);
    }

    private void previousMonth() {
        this.calendar.roll(Calendar.MONTH, false);
        this.refreshCalendar(this.calendar);
    }
}
