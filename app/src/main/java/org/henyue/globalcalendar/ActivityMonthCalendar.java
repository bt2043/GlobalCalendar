package org.henyue.globalcalendar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class ActivityMonthCalendar extends Activity {

    private Calendar calendar = Calendar.getInstance();
    private Calendar current = Calendar.getInstance();
    private float fontUnit;

    private int rowIndex;
    private int colIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = super.getResources().getDisplayMetrics();
        this.fontUnit = dm.scaledDensity;

        LinearLayout linearLayout = this.initBasicActivity();

        super.setContentView(linearLayout);

    }

    private LinearLayout initBasicActivity() {
        LinearLayout linearLayout = this.createMatchParentLinearLayout(LinearLayout.VERTICAL, Gravity.TOP);
        linearLayout.setBackgroundResource(R.drawable.bg_month_view);

        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;     // 屏幕宽度（像素）
        int dateColLen = screenWidth/8;         //按屏幕宽度均分8等份，7份为日历周日到周六，1份宽度为两边留白

        LinearLayout weekHeader = this.initWeekHeader(dateColLen);
        FrameLayout mainCalendar = this.initialMonthCalendar(dateColLen);

        //Add weekHeader and mainCalendar to mainActivity
        linearLayout.addView(weekHeader);
        linearLayout.addView(mainCalendar);
        return linearLayout;
    }

    private LinearLayout initWeekHeader(int dateColLen) {
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

    private FrameLayout initialMonthCalendar(int dateColLen) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setAlpha(0.9f);
        //TODO set main calendar's background resource
        //tableLayout.setBackgroundResource(R.drawable.bg_month_view_calendar_bmp);
        this.generateCalendar(dateColLen, tableLayout);

        FrameLayout calendarContainer = new FrameLayout(this);
        calendarContainer.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));
        //Background Month Text
        TextView monthNumber = new TextView(this);
        monthNumber.setTextSize(180f);
        monthNumber.setTextColor(Color.WHITE);
        monthNumber.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        monthNumber.setAlpha(0.9f);
        monthNumber.setTypeface(Typeface.MONOSPACE);
        monthNumber.setGravity(Gravity.CENTER_HORIZONTAL);
        //TODO to be removed after set main calendar's background
        calendarContainer.setBackgroundColor(Color.parseColor("#BBFFFFFF"));
        calendarContainer.addView(monthNumber);
        calendarContainer.addView(tableLayout);
        return calendarContainer;
    }

    private void generateCalendar(int dateColLen, TableLayout calendarLayout) {
        DateTextView[][] dateViews = new DateTextView[6][7];
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int day = 0;

        boolean isEndOfMonth = false;

        for (int i = 0; i < dateViews.length; i++) {
            if (isEndOfMonth) {
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
                    dateViews[i][j].setTextColor(Color.parseColor("#388A13"));
                    dateViews[i][j].setTypeface(Typeface.MONOSPACE);
                    dateViews[i][j].setTextSize(22f);

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

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
