package org.henyue.globalcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by henyue on 2014/7/22.
 */
public class DateTextView extends TextView {
    public DateTextView(Context context) {
        super(context);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        int stopX = this.getWidth() - 1;
        int stopY = this.getHeight() - 1;
        //Draw border
        canvas.drawLine(0, 0, stopX, 0, paint);
        canvas.drawLine(0, 0, 0, stopY, paint);
        canvas.drawLine(stopX, 0, stopX, stopY, paint);
        canvas.drawLine(0, stopY, stopX, stopY, paint);

    }
}
