package org.henyue.globalcalendar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

    private int rowIndex;
    private int colIndex;

    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.resources = super.getResources();

        LinearLayout linearLayout = this.initBasicActivity();

        super.setContentView(linearLayout);

    }

    private LinearLayout initBasicActivity() {
        LinearLayout linearLayout = this.createMatchParentLinearLayout(LinearLayout.VERTICAL, Gravity.TOP);
        linearLayout.setBackgroundResource(R.drawable.bg_month_view);

        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;     // 屏幕宽度（像素）
        int dateColLen = screenWidth/10;

        LinearLayout weekHeader = this.initWeekHeader(dateColLen);
        LinearLayout mainCalendar = this.initialMonthCalendar(dateColLen);

        //Add weekHeader and mainCalendar to mainActivity
        linearLayout.addView(weekHeader);
        linearLayout.addView(mainCalendar);
        return linearLayout;
    }

    private LinearLayout initWeekHeader(int dateColLen) {
        TableLayout tableLayout = new TableLayout(this);
        TableRow weekRow = new TableRow(this);
        weekRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        String[] weeks = resources.getStringArray(R.array.week);
        for (String week : weeks) {
            TextView weekLabel = new TextView(this);
            weekLabel.setText(week);
            weekLabel.setTextColor(Color.parseColor("#388A13"));
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
        weekRowContainer.setBackgroundResource(R.drawable.bg_month_view_week_bmp);
        weekRowContainer.addView(tableLayout);
        return weekRowContainer;
    }

    private LinearLayout initialMonthCalendar(int dateColLen) {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundResource(R.drawable.bg_month_view_calendar_bmp);

        DateTextView[][] dateViews = new DateTextView[6][7];
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = 0;
        boolean isEndOfMonth = false;

        for (int i = 0; i < dateViews.length; i++) {
            TableRow dateRow = new TableRow(this);
            dateRow.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            for (int j = 0; j < dateViews[i].length; j++) {
                dateViews[i][j] = new DateTextView(this);
                dateViews[i][j].setGravity(Gravity.CENTER);
                dateViews[i][j].setWidth(dateColLen);
                dateViews[i][j].setHeight(dateColLen);
                if (j >= week - 1) {
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    dateViews[i][j].setText(String.valueOf(day));
                    dateViews[i][j].setTextColor(Color.parseColor("#388A13"));

                    if (calendar.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
                            && calendar.get(Calendar.YEAR) == current.get(Calendar.YEAR)
                            && !isEndOfMonth) {
                        dateViews[i][j].setBackgroundResource(R.drawable.bg_selected_date);
                        rowIndex = i;
                        colIndex = j;
                    }

                    if (isEndOfMonth) {
                        dateViews[i][j].setVisibility(View.INVISIBLE);
                    }

                    if (calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        isEndOfMonth = true;
                    }
                    calendar.roll(Calendar.DAY_OF_MONTH, true);
                }
                dateRow.addView(dateViews[i][j], new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                ));
            }
            week = 1;   //every row should start from sunday except the first week row
            tableLayout.addView(dateRow, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
        }

        LinearLayout mainContainer = this.createMatchParentLinearLayout(LinearLayout.HORIZONTAL, Gravity.TOP);
        mainContainer.setBackgroundResource(R.drawable.bg_month_view_calendar_bmp);
        mainContainer.addView(tableLayout);
        return mainContainer;
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
}
