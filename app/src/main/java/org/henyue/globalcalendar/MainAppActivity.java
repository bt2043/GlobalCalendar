package org.henyue.globalcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import org.henyue.globalcalendar.utils.NumberEditText;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Kong on 2014/8/18.
 */
public class MainAppActivity extends Activity {
    private ActivityMonthCalendar activityMonthCalendar;

    private ScrollView mainView;
    private ImageView menuCursor;
    private int currIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.main_app_layout);

        //Initial group button checked flag image
        menuCursor = (ImageView) findViewById(R.id.menu_cursor);
        final int bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.menu_on_cursor).getWidth();;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        final int paddingW = (screenW / 4 -bmpW) / 2;//计算前后空白长度
        Matrix matrix = new Matrix();
        matrix.postTranslate(paddingW, 0);
        menuCursor.setImageMatrix(matrix);

        this.activityMonthCalendar = new ActivityMonthCalendar(this);
        mainView = (ScrollView) this.findViewById(R.id.main_app_view);
        mainView.addView(this.activityMonthCalendar);

        RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.main_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int perMenuLen = paddingW * 2 +bmpW;

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int resourceId) {
                int checkedIdx = getCheckedBtnIndexById(resourceId);
                Log.d("MainAppActivity", "Radio button onCheckedChanged: currIndex=" + currIndex + ", checkedIdx=" + checkedIdx);

                int fromX = perMenuLen * currIndex;
                int targetX = perMenuLen * checkedIdx;
                Animation animation = new TranslateAnimation(fromX, targetX, 0, 0);;
                currIndex = checkedIdx;

                animation.setFillAfter(true);
                animation.setDuration(300);
                menuCursor.startAnimation(animation);
            }
        });
    }

    private int getCheckedBtnIndexById(int resourceId) {
        switch (resourceId) {
            case R.id.bottom_menu_activities: return 1;
            case R.id.bottom_menu_post: return 2;
            case R.id.bottom_menu_settings: return 3;
            default: return 0;
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
        } else if (id == R.id.action_jumptotoday) {
            activityMonthCalendar.jumpToToday();
            return true;
        } else if (id == R.id.action_jumptoday) {
            createJumpToDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createJumpToDialog() {
        final View layout = getLayoutInflater().inflate(R.layout.jump_to_dialog, (ViewGroup) findViewById(R.id.jump_to_dialog));
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.jump_to_intro_title).setView(layout);
        builder.setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmJumpToDay(layout);
            }
        });
        builder.setNegativeButton(R.string.action_cancel, null);
        builder.show();
    }

    private void confirmJumpToDay(View layout) {
        NumberEditText jumpToYearTV = (NumberEditText) layout.findViewById(R.id.jumpToYear);
        NumberEditText jumpToMonthTV = (NumberEditText) layout.findViewById(R.id.jumpToMonth);
        NumberEditText jumpToDayTV = (NumberEditText) layout.findViewById(R.id.jumpToDay);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, jumpToYearTV.getNumber());
        cal.set(Calendar.MONTH, jumpToMonthTV.getNumber() - 1);
        Log.d("confirmJumpToDay", "cal.getMaximum() = " + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (jumpToDayTV.getNumber() > cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            Toast.makeText(MainAppActivity.this, R.string.dateNotExisted, Toast.LENGTH_LONG);
        } else {
            this.activityMonthCalendar.jumpToDate(jumpToYearTV.getNumber(), jumpToMonthTV.getNumber(), jumpToDayTV.getNumber());
        }
    }
}
