package com.stadium.app.models.entities;

import com.stadium.app.models.Checkable;

/**
 * Created by Shamyyoun on 11/4/16.
 */

public class OrderCriteria implements Checkable {
    public static final int TYPE_RATE = 1;
    public static final int TYPE_NAME = 2;

    private int type;
    private String name;
    private boolean checked;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public String toString() {
        return name;
    }
}
