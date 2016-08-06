package com.stadium.app.models.entities;

import com.stadium.app.models.enums.TeamClass;

/**
 * Created by karam on 7/19/16.
 */
public class PlayerTeams {

    private String title;
    private String logo;
    private TeamClass teamClass;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public TeamClass getTeamClass() {
        return teamClass;
    }

    public void setTeamClass(TeamClass teamClass) {
        this.teamClass = teamClass;
    }
}
