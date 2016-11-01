package com.stadium.app.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum PresentConfirmType {
    DECLINE(0), CONFIRM(1);

    private int value;

    PresentConfirmType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
