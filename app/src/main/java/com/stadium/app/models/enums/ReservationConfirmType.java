package com.stadium.app.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum ReservationConfirmType {
    NO_ACTION(0), DECLINE(1), CONFIRM(2), PENDING(3);

    private int value;

    ReservationConfirmType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
