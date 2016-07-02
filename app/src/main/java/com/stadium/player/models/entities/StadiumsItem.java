package com.stadium.player.models.entities;

/**
 * Created by karam on 7/2/16.
 */
public class StadiumsItem {

    private String stadium;

    public StadiumsItem(String name) {
        this.stadium = name;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}
