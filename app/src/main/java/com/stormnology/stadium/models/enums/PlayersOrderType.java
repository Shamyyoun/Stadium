package com.stormnology.stadium.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum PlayersOrderType {
    STADIUM_RESERVED(2);

    private int value;

    PlayersOrderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
