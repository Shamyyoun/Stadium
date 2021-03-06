package com.stormnology.stadium.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum EventType {
    STADIUM_RESERVED(2), NEW_CHALLENGE(13);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
