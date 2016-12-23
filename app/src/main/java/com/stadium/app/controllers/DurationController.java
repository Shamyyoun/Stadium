package com.stadium.app.controllers;

import com.stadium.app.models.entities.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class DurationController {

    public List<Duration> createList(String[] durationsArr) {
        List<Duration> durations = new ArrayList<>(durationsArr.length);
        for (int i = 0; i < durationsArr.length; i++) {
            Duration duration = new Duration();
            String[] startEnd = durationsArr[i].split("-");
            if (startEnd.length == 2) {
                duration.setStartTime(startEnd[0]);
                duration.setEndTime(startEnd[1]);
            } else {
                duration.setStartTime(" ");
                duration.setEndTime(" ");
            }
            durations.add(duration);
        }

        return durations;
    }

    public int getItemPosition(List<Duration> durations, String timeStart, String timeEnd) {
        if (timeStart == null || timeEnd == null) {
            return -1;
        }

        for (int i = 0; i < durations.size(); i++) {
            if (timeStart.equals(durations.get(i).getStartTime())
                    && timeEnd.equals(durations.get(i).getEndTime())) {
                return i;
            }
        }

        return -1;
    }

    public int getItemPosition(List<Duration> durations, int durationId) {
        for (int i = 0; i < durations.size(); i++) {
            if (durationId == durations.get(i).getId()) {
                return i;
            }
        }

        return -1;
    }

    public List<Duration> addDefaultItem(List<Duration> durations, String defaultItemName) {
        if (durations == null) {
            durations = new ArrayList<>();
        }

        // add the default item
        Duration duration = new Duration();
        duration.setDefaultName(defaultItemName);
        durations.add(0, duration);

        return durations;
    }
}
