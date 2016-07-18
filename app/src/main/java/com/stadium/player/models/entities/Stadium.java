package com.stadium.player.models.entities;

/**
 * Created by karam on 7/2/16.
 */
public class Stadium {

    private String title ;

    public Stadium(String name) {
        this.title  = name;
    }

    public String getStadium() {
        return title ;
    }

    public void setStadium(String stadium) {
        this.title  = stadium;
    }
}
