package com.stormnology.stadium.controllers;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.models.entities.Duration;
import com.stormnology.stadium.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    /**
     * method, used to check times in durations and return true if filled or false or otherwise.
     *
     * @param durations
     * @return
     */
    public boolean checkDurationsFilled(List<Duration> durations) {
        for (Duration duration : durations) {
            if (duration.getStartTime() == null || duration.getEndTime() == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * method, used to check two times and return true if end time is higher 1 higher than start time
     * or false otherwise.
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean checkTimesValid(String startTime, String endTime) {
        long differenceInMillis = DateUtils.difference(startTime, endTime, Const.SER_TIME_FORMAT);
        long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMillis);


        return (differenceInHours >= Const.STADIUM_DURATIONS_MIN_HOURS);
    }

    /**
     * method, used to check times in all durations and return true if valid or false if nested durations found.
     *
     * @return
     */
    public boolean checkNoNestedDurations(List<Duration> durations) {
        // check size
        if (durations.size() <= 1) {
            // has zero or one duration,
            // so must be true
            return true;
        }

        // loop all durations
        for (int i = 0; i < durations.size(); i++) {
            // get the duration to check it
            Duration duration = durations.get(i);

            // loop all other durations to check duration with them
            for (int j = 0; j < durations.size(); j++) {
                // get other duration
                Duration otherDuration = durations.get(j);

                // check if it is the same duration, continue
                if (i == j) {
                    continue;
                }

                // prepare comparing results
                int compareRes1 = DateUtils.compare(otherDuration.getStartTime(), duration.getStartTime(), Const.SER_TIME_FORMAT);
                int compareRes2 = DateUtils.compare(otherDuration.getStartTime(), duration.getEndTime(), Const.SER_TIME_FORMAT);
                int compareRes3 = DateUtils.compare(otherDuration.getEndTime(), duration.getStartTime(), Const.SER_TIME_FORMAT);
                int compareRes4 = DateUtils.compare(otherDuration.getEndTime(), duration.getStartTime(), Const.SER_TIME_FORMAT);

                // check result
                // validation based on the negative probability,
                // if the negative probability found return false or continue in the loop
                if (compareRes1 >= 0 && compareRes2 == -1
                        || compareRes3 == 1 && compareRes4 <= 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
