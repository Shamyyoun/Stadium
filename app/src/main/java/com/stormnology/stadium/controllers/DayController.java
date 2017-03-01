package com.stormnology.stadium.controllers;

import android.content.Context;

import com.stormnology.stadium.R;
import com.stormnology.stadium.models.entities.Day;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 5/24/16.
 */
public class DayController {
    public static final int DAY_SAT = 1;
    public static final int DAY_SUN = 2;
    public static final int DAY_MON = 3;
    public static final int DAY_TUE = 4;
    public static final int DAY_WED = 5;
    public static final int DAY_THUR = 6;
    public static final int DAY_FRI = 7;

    public List<Day> getDays(Context context) {
        List<Day> days = new ArrayList<>();

        Day day1 = new Day();
        day1.setId(DAY_SAT);
        day1.setTitle(context.getString(R.string.saturday));
        day1.setValue("Saturday");
        days.add(day1);

        Day day2 = new Day();
        day2.setId(DAY_SUN);
        day2.setTitle(context.getString(R.string.sunday));
        day2.setValue("Sunday");
        days.add(day2);

        Day day3 = new Day();
        day3.setId(DAY_MON);
        day3.setTitle(context.getString(R.string.monday));
        day3.setValue("Monday");
        days.add(day3);

        Day day4 = new Day();
        day4.setId(DAY_TUE);
        day4.setTitle(context.getString(R.string.tuesday));
        day4.setValue("Tuesday");
        days.add(day4);

        Day day5 = new Day();
        day5.setId(DAY_WED);
        day5.setTitle(context.getString(R.string.wednesday));
        day5.setValue("Wednesday");
        days.add(day5);

        Day day6 = new Day();
        day6.setId(DAY_THUR);
        day6.setTitle(context.getString(R.string.thursday));
        day6.setValue("Thursday");
        days.add(day6);

        Day day7 = new Day();
        day7.setId(DAY_FRI);
        day7.setTitle(context.getString(R.string.friday));
        day7.setValue("Friday");
        days.add(day7);

        return days;
    }

    public int getItemPosition(List<Day> days, int dayId) {
        for (int i = 0; i < days.size(); i++) {
            if (dayId == days.get(i).getId()) {
                return i;
            }
        }

        return -1;
    }
}
