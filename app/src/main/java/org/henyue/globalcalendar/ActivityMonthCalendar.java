package org.henyue.globalcalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.henyue.globalcalendar.utils.DateTextView;
import org.henyue.globalcalendar.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityMonthCalendar extends RelativeLayout {

    private Context context;
    private Activity rootActivity;
    private TableLayout calendarLayout;
    private TextView monthNumber;
    private int dateColLen;
    private Calendar calendar;
    private Date preSelectedDate;
    private Date selectedDate;
    private Map<Date, DateTextView> dateViewMap;

    private static final int COLOR_SELECTED_DATE = Color.RED;
    private static final int COLOR_NORMAL_DATE = Color.parseColor("#388A13");

    private AnimationSet nextInAnimationSet;
    private AnimationSet preInAnimationSet;
    private AnimationSet nextOutAnimationSet;
    private AnimationSet preOutAnimationSet;
    private AnimationSet jumpToInAnimationSet;
    private AnimationSet jumpToOutAnimationSet;
    private Calendar jumpToCalendar;
    private static final int animationDuration = 300;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector calendarGesture;
    private View.OnTouchListener onTouchListener;

    public ActivityMonthCalendar(Activity rootActivity) {
        super(rootActivity);
        this.context = rootActivity;
        this.rootActivity = rootActivity;
        this.nextOutAnimationSet = createAnimationSet(animationDuration, false, true);
        this.preOutAnimationSet = createAnimationSet(animationDuration, false, false);
        this.jumpToOutAnimationSet = createJumpToAnimationSet(animationDuration, false);
        this.nextInAnimationSet = this.createNextInAnimationSet();
        this.preInAnimationSet = this.createPreInAnimationSet();
        this.jumpToInAnimationSet = this.createJumpToInAnimationSet();
        this.init();

        this.calendarGesture = new GestureDetector(this.context, new GestureListener());
        this.onTouchListener = new CalendarTouchListener();
        //this.setOnTouchListener(this.onTouchListener);
    }

    protected void init() {
        this.calendar = Calendar.getInstance();
        this.monthNumber = new TextView(this.context);
        this.calendarLayout = new TableLayout(this.context);
        this.dateViewMap = new HashMap<Date, DateTextView>();
        calendarLayout.setAlpha(0.9f);
        //TODO set main calendar's background resource
        //calendarLayout.setBackgroundResource(R.drawable.bg_month_view_calendar_bmp);

        LinearLayout linearLayout = this.initBasicActivity();
        linearLayout.setLongClickable(true);
        linearLayout.setOnTouchListener(this.onTouchListener);
        this.addView(linearLayout);
    }

    private LinearLayout initBasicActivity() {
        LinearLayout linearLayout = this.createMatchParentLinearLayout(LinearLayout.VERTICAL, Gravity.TOP);
        //linearLayout.setBackgroundResource(R.drawable.bg_month_view);

        DisplayMetrics metrics = new DisplayMetrics();
        this.rootActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
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
        TableLayout tableLayout = new TableLayout(context);
        TableRow weekRow = new TableRow(context);
        weekRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        String[] weeks = super.getResources().getStringArray(R.array.week);
        for (String week : weeks) {
            TextView weekLabel = new TextView(context);
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

        FrameLayout calendarContainer = new FrameLayout(context);
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
            TableRow dateRow = new TableRow(context);
            dateRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            for (int j = 0; j < dateViews[i].length; j++) {

                dateViews[i][j] = new DateTextView(this.context, this);
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

    private LinearLayout createMatchParentLinearLayout(int orientation, int verticalGravity) {
        LinearLayout container = new LinearLayout(context);
        container.setOrientation(orientation);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        container.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        container.setVerticalGravity(verticalGravity);
        return container;
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

    public void nextMonth() {
        this.startAnimation(nextInAnimationSet);
    }

    public void previousMonth() {
        this.startAnimation(preInAnimationSet);
    }

    public void jumpToDate(int year, int month, int day) {
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.set(Calendar.YEAR, year);
        targetCalendar.set(Calendar.MONTH, month - 1);
        targetCalendar.set(Calendar.DAY_OF_MONTH, day);
        this.jumpToCalendar = targetCalendar;
        this.selectedDate = DateUtil.getCleanDate(targetCalendar);
        this.startAnimation(jumpToInAnimationSet);
    }

    public void jumpToToday() {
        this.jumpToCalendar = Calendar.getInstance();
        this.selectedDate = DateUtil.getCleanDate(this.jumpToCalendar);
        this.startAnimation(jumpToInAnimationSet);
    }

    private AnimationSet createNextInAnimationSet() {
        AnimationSet animationSet = createAnimationSet(animationDuration, true, true);
        animationSet.setAnimationListener(this.createCalendarAnimationListener());
        return animationSet;
    }

    private AnimationSet createPreInAnimationSet() {
        AnimationSet animationSet = createAnimationSet(animationDuration, true, false);
        animationSet.setAnimationListener(this.createCalendarAnimationListener());
        return animationSet;
    }

    private static AnimationSet createAnimationSet(int duration, boolean isInAnimation, boolean isTargetLeft) {
        float fromScale = isInAnimation ? 1F : 0.6F;
        float toScale = isInAnimation ? 0.6F : 1F;
        AnimationSet animationSet = createStepAnimationSet(duration, fromScale, toScale, isInAnimation, isTargetLeft);
        return animationSet;
    }

    private static AnimationSet createStepAnimationSet(int duration, float fromScale, float toScale, boolean isInAnimation, boolean isToLeft) {
        AnimationSet animationSet = new AnimationSet(true);
        int durationVal = isInAnimation ? duration : duration - 100;
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromScale, toScale, fromScale, toScale,
                Animation.RELATIVE_TO_SELF, 0F,
                Animation.RELATIVE_TO_SELF, 0F);
        scaleAnimation.setDuration(durationVal);
        animationSet.addAnimation(scaleAnimation);

        int direct = isToLeft ? -1 : 1;
        float fromXValue = isInAnimation ? 0 : direct * -1F;
        float toXValue = isInAnimation ? 1F * direct : 0;
        TranslateAnimation transAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, fromXValue,
                Animation.RELATIVE_TO_SELF, toXValue,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        transAnimation.setDuration(durationVal);
        animationSet.addAnimation(transAnimation);

        int startAlpha = isInAnimation ? 1 : 0;
        int endAlpha = isInAnimation ? 0 : 1;
        AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, endAlpha);
        alphaAnimation.setDuration(durationVal);
        animationSet.addAnimation(alphaAnimation);

        return animationSet;
    }

    private AnimationSet createJumpToInAnimationSet() {
        AnimationSet animationSet = this.createJumpToAnimationSet(animationDuration, true);
        animationSet.setAnimationListener(this.createCalendarAnimationListener());
        return animationSet;
    }

    private AnimationSet createJumpToAnimationSet(int duration, boolean isInAnimation) {
        int durationVal = duration;
        if (!isInAnimation && duration > 100) {
            durationVal = duration - 100;
        }

        AnimationSet animationSet = new AnimationSet(true);
        float fromY = isInAnimation ? 0F : -0.3F;
        float toY = isInAnimation ? -0.3F : 0F;
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0F,
                Animation.RELATIVE_TO_SELF, 0F,
                Animation.RELATIVE_TO_SELF, fromY,
                Animation.RELATIVE_TO_SELF, toY);
        translateAnimation.setDuration(durationVal);
        animationSet.addAnimation(translateAnimation);

        int fromAlpha = isInAnimation ? 1 : 0;
        int toAlpha = isInAnimation ? 0 : 1;
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(durationVal);
        animationSet.addAnimation(alphaAnimation);

        return animationSet;
    }

    private Animation.AnimationListener createCalendarAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("animation", "current animation is " + animation);
                AnimationSet nextAnimationSet = null;
                if (animation == nextInAnimationSet) {
                    calendar.add(Calendar.MONTH, 1);
                    nextAnimationSet = nextOutAnimationSet;
                } else if (animation == preInAnimationSet) {
                    calendar.add(Calendar.MONTH, -1);
                    nextAnimationSet = preOutAnimationSet;
                } else if (animation == jumpToInAnimationSet) {
                    calendar = jumpToCalendar;
                    nextAnimationSet = jumpToOutAnimationSet;
                }
                refreshCalendar(calendar);
                if (nextAnimationSet != null) startAnimation(nextAnimationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Ignore
            }
        };
    }

    private class CalendarTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return calendarGesture.onTouchEvent(motionEvent);
        }
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                nextMonth(); // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                previousMonth(); // Left to right
            }
            return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (calendarGesture.onTouchEvent(ev)) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(ev);
    }
}
