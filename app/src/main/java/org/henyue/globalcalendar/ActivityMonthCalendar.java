package org.henyue.globalcalendar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private TextView currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.resources = super.getResources();

        LinearLayout linearLayout = this.initBasicActivity();

        super.setContentView(linearLayout);
    }

    private LinearLayout initBasicActivity() {
        LinearLayout linearLayout = new LinearLayout(this);
        //initial the header for week
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        currentMonth = new TextView(this);
        currentMonth.setText(current.get(Calendar.YEAR) + "-" + (current.get(Calendar.MONTH) + 1));
        currentMonth.setGravity(Gravity.CENTER);
        linearLayout.addView(currentMonth);

        TableLayout tableLayout = this.initialMonthCalendar();
        //TODO
        linearLayout.addView(tableLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        return linearLayout;
    }

    private TableLayout initialMonthCalendar() {
        TableLayout tableLayout = new TableLayout(this);
        TableRow weekRow = new TableRow(this);
        String[] weeks = resources.getStringArray(R.array.week);
        for (String week : weeks) {
            TextView weekLabel = new TextView(this);
            weekLabel.setText(week);
            weekLabel.setGravity(Gravity.CENTER);
            weekLabel.setPadding(0, 0, 7, 28);
            weekRow.addView(weekLabel);
        }
        tableLayout.addView(weekRow, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
        ));

        DateTextView[][] dateViews = new DateTextView[6][7];
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = 0;
        boolean isEndOfMonth = false;

        for (int i = 0; i < dateViews.length; i++) {
            TableRow dateRow = new TableRow(this);

            for (int j = 0; j < dateViews[i].length; j++) {
                dateViews[i][j] = new DateTextView(this);
                if (j >= week - 1) {
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    dateViews[i][j].setText(String.valueOf(day));

                    if (calendar.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
                            && calendar.get(Calendar.YEAR) == current.get(Calendar.YEAR)
                            && !isEndOfMonth) {
                        dateViews[i][j].setBackgroundColor(Color.RED);
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
        return tableLayout;
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
}
