package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.ChallengeTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class ChallengeTimeController {

    public List<ChallengeTime> createList(String[] timesArr) {
        List<ChallengeTime> times = new ArrayList<>(timesArr.length);
        for (int i = 0; i < timesArr.length; i++) {
            ChallengeTime time = new ChallengeTime();
            time.setName(timesArr[i]);
            times.add(time);
        }

        return times;
    }

    public int getItemPosition(List<ChallengeTime> times, String challengeTime) {
        if (challengeTime == null) {
            return -1;
        }

        for (int i = 0; i < times.size(); i++) {
            if (challengeTime.equals(times.get(i).getName())) {
                return i;
            }
        }

        return -1;
    }

    public List<ChallengeTime> addDefaultItem(List<ChallengeTime> times, String defaultItemName) {
        if (times == null) {
            times = new ArrayList<>();
        }

        // add the default item
        ChallengeTime time = new ChallengeTime();
        time.setName(defaultItemName);
        times.add(0, time);

        return times;
    }
}
