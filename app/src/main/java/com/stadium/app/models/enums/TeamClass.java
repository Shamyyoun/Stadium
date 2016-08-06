package com.stadium.app.models.enums;

import com.stadium.app.R;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum TeamClass {
    A("A", R.drawable.green_circle), C("C", R.drawable.orange_circle);

    private String title;
    private int colorId;

    TeamClass(String title, int colorId) {
        this.title = title;
        this.colorId = colorId;
    }

    public String getTitle() {
        return title;
    }

    public int getColorId() {
        return colorId;
    }
}
