package org.henyue.globalcalendar.beans.event;

import android.graphics.Color;

/**
 * Created by henyue on 2014/7/30.
 */
public class Region {
    public static final Region NORMAL = new Region(Color.WHITE);
    public static final Region CHINA = new Region(Color.RED);
    public static final Region INDIA = new Region(Color.YELLOW);
    public static final Region US = new Region(Color.GREEN);
    public static final Region UK = new Region(Color.BLUE);

    public int color;
    public Region(int color) {
        this.color = color;
    }
}
