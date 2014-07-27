package org.henyue.globalcalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

/**
 * Created by henyue on 2014/7/22.
 */
public class DateTextView extends TextView {

    public DateTextView(Context context) {
        super(context);
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
