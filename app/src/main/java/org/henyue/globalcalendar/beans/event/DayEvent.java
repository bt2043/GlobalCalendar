package org.henyue.globalcalendar.beans.event;

import java.io.Serializable;

/**
 * Created by henyue on 2014/7/30.
 */
public class DayEvent implements Serializable {
    private String name;
    private String desc;
    private Region region;

    public DayEvent(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public DayEvent(String name, String desc, Region region) {
        this(name, desc);
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
