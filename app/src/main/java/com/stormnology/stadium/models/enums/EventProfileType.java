package com.stormnology.stadium.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum EventProfileType {
    PLAYER(1), TEAM(2), STADIUM(3);

    private int value;

    EventProfileType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}