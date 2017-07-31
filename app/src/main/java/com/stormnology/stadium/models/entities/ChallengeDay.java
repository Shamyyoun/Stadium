package com.stormnology.stadium.models.entities;

import com.stormnology.stadium.models.Checkable;

import java.io.Serializable;

public class ChallengeDay implements Serializable, Checkable {
    private String name;
    private boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }
}
