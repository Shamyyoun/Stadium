package com.stadium.app.controllers;

import com.stadium.app.models.entities.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class TimeController {

    public List<Time> createList(String[] timesArr) {
        List<Time> times = new ArrayList<>(timesArr.length);
        for (int i = 0; i < timesArr.length; i++) {
            Time time = new Time();
            String[] startEnd = timesArr[i].split("-");
            if (startEnd.length == 2) {
                time.setStart(startEnd[0]);
                time.setEnd(startEnd[1]);
            } else {
                time.setStart(" ");
                time.setEnd(" ");
            }
            times.add(time);
        }

        return times;
    }

    public int getItemPosition(List<Time> times, String timeStart, String timeEnd) {
        if (timeStart == null || timeEnd == null) {
            return -1;
        }

        for (int i = 0; i < times.size(); i++) {
            if (timeStart.equals(times.get(i).getStart())
                    && timeEnd.equals(times.get(i).getEnd())) {
                return i;
            }
        }

        return -1;
    }

    public List<Time> addDefaultItem(List<Time> times, String defaultItemName) {
        if (times == null) {
            times = new ArrayList<>();
        }

        // add the default item
        Time time = new Time();
        time.setDefaultName(defaultItemName);
        times.add(0, time);

        return times;
    }
}
