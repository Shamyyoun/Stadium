package com.stadium.app.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum ReservationConfirmType {
    DECLINE(0), CONFIRM(1);

    private int value;

    ReservationConfirmType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
