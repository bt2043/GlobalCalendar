package org.henyue.globalcalendar.beans.event;

/**
 * Created by henyue on 2014/7/30.
 */
public class TeamActivity extends DayEvent {
    private String teamName;

    public TeamActivity(String name, String teamName, String desc) {
        super(name, desc);
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
