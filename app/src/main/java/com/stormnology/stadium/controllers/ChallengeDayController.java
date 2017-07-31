package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.ChallengeDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class ChallengeDayController {

    public List<ChallengeDay> createList(String[] daysArr) {
        List<ChallengeDay> days = new ArrayList<>(daysArr.length);
        for (int i = 0; i < daysArr.length; i++) {
            ChallengeDay day = new ChallengeDay();
            day.setName(daysArr[i]);
            days.add(day);
        }

        return days;
    }

    public int getItemPosition(List<ChallengeDay> days, String challengeDay) {
        if (challengeDay == null) {
            return -1;
        }

        for (int i = 0; i < days.size(); i++) {
            if (challengeDay.equals(days.get(i).getName())) {
                return i;
            }
        }

        return -1;
    }

    public List<ChallengeDay> addDefaultItem(List<ChallengeDay> days, String defaultItemName) {
        if (days == null) {
            days = new ArrayList<>();
        }

        // add the default item
        ChallengeDay day = new ChallengeDay();
        day.setName(defaultItemName);
        days.add(0, day);

        return days;
    }
}
