package com.stadium.player.models.entities;

import com.stadium.player.models.Checkable;

/**
 * Created by karam on 7/2/16.
 */
public class Stadium extends Checkable {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
