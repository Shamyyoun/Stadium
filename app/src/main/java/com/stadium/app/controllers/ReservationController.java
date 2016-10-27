package com.stadium.app.controllers;

import com.stadium.app.models.entities.Reservation;
import com.stadium.app.utils.DateUtils;

/**
 * Created by Shamyyoun on 10/27/16.
 */

public class ReservationController {
    public String getDateTime(Reservation reservation) {
        String dateTime = "";

        String date = DateUtils.formatDate(reservation.getDate(), Reservation.DATE_FORMAT, "d-M-yyyy");
        dateTime += date;

        String endTime = DateUtils.formatDate(reservation.getTimeEnd(), Reservation.TIME_FORMAT, "a hh:mm");
        dateTime += " . " + endTime;

        String startTime = DateUtils.formatDate(reservation.getTimeStart(), Reservation.TIME_FORMAT, "a hh:mm");
        dateTime += " - " + startTime;

        return dateTime;
    }
}
