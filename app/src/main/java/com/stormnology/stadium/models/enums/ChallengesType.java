package com.stormnology.stadium.models.enums;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum ChallengesType {
    NEW_CHALLENGES(1), ACCEPTED_CHALLENGES(2),
    HISTORICAL_CHALLENGES(3), MY_CHALLENGES(4);

    private int value;

    ChallengesType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
