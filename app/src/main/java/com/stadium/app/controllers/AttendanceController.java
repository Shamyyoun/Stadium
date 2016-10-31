package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.models.entities.Attendant;
import com.stadium.app.models.entities.User;

import java.util.List;

/**
 * Created by Shamyyoun on 10/31/16.
 */

public class AttendanceController {
    public static List<Attendant> orderAttendance(Context context, List<Attendant> attendance) {
        // get the active user
        User activeUser = new ActiveUserController(context).getUser();

        // loop the attendance to order
        for (int i = 0; i < attendance.size(); i++) {
            Attendant attendant = attendance.get(i);
            if (activeUser.getId() == attendant.getPlayer().getId()) {
                attendance.remove(i);
                attendance.add(0, attendant);
            }
        }

        return attendance;
    }

    public static boolean isCurrentActiveUser(Attendant attendant, int activeUserId) {
        return (activeUserId == attendant.getPlayer().getId());
    }
}
