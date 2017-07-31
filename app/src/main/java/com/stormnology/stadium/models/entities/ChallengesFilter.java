package com.stormnology.stadium.models.entities;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 7/30/17.
 */

public class ChallengesFilter implements Serializable {
    private ChallengeType type;
    private Team team;
    private Stadium place;
    private String day;
    private String time;

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Stadium getPlace() {
        return place;
    }

    public void setPlace(Stadium place) {
        this.place = place;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
