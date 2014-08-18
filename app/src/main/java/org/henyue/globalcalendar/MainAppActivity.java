package org.henyue.globalcalendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

/**
 * Created by Kong on 2014/8/18.
 */
public class MainAppActivity extends Activity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector calendarGesture;
    private View.OnTouchListener onTouchListener;
    private ActivityMonthCalendar activityMonthCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.main_app_layout);

        this.activityMonthCalendar = new ActivityMonthCalendar(this);
        ScrollView mainView = (ScrollView) this.findViewById(R.id.main_app_view);
        mainView.addView(this.activityMonthCalendar);

        this.calendarGesture = new GestureDetector(this, new GestureListener(this.activityMonthCalendar));
        this.onTouchListener = new CalendarTouchListener();
        mainView.setOnTouchListener(this.onTouchListener);

        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.main_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            private final int SELECTED_COLOR = Color.parseColor("#61388A13");
            private final int UNSELECTED_COLOR = Color.parseColor("#66FFFFFF");

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton monthViewRB = (RadioButton) findViewById(R.id.bottom_menu_monthview);
                RadioButton activitiesRB = (RadioButton) findViewById(R.id.bottom_menu_activities);
                RadioButton postRB = (RadioButton) findViewById(R.id.bottom_menu_post);
                RadioButton profileRB = (RadioButton) findViewById(R.id.bottom_menu_settings);

                monthViewRB.setBackgroundColor(UNSELECTED_COLOR);
                activitiesRB.setBackgroundColor(UNSELECTED_COLOR);
                postRB.setBackgroundColor(UNSELECTED_COLOR);
                profileRB.setBackgroundColor(UNSELECTED_COLOR);

                switch (i) {
                    case R.id.bottom_menu_monthview:
                        monthViewRB.setBackgroundColor(SELECTED_COLOR);
                        break;
                    case R.id.bottom_menu_activities:
                        activitiesRB.setBackgroundColor(SELECTED_COLOR);
                        break;
                    case R.id.bottom_menu_post:
                        postRB.setBackgroundColor(SELECTED_COLOR);
                        break;
                    case R.id.bottom_menu_settings:
                        profileRB.setBackgroundColor(SELECTED_COLOR);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class CalendarTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return calendarGesture.onTouchEvent(motionEvent);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private ActivityMonthCalendar calendar;
        public GestureListener(ActivityMonthCalendar calendar) {
            this.calendar = calendar;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                this.calendar.nextMonth();
                return true; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                this.calendar.previousMonth();
                return true; // Left to right
            }

            return false;
        }
    }
}
