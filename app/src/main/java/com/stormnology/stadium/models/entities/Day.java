package com.stormnology.stadium.models.entities;

import com.stormnology.stadium.models.Checkable;

/**
 * Created by Shamyyoun on 12/23/16.
 */

public class Day implements Checkable {
    private int id;
    private String title;
    private String value;
    private boolean checked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return title;
    }
}
