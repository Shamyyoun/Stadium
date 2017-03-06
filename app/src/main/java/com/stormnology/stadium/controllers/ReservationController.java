package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

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

    public String getFieldNumber(Reservation reservation) {
        if (reservation.getField() != null && !Utils.isNullOrEmpty(reservation.getField().getFieldNumber())) {
            return reservation.getField().getFieldNumber();
        } else {
            return null;
        }
    }

    public String getTeamStadiumName(Reservation reservation) {
        Team team = reservation.getReservationTeam();
        Stadium stadium = reservation.getReservationStadium();

        String text = "";
        if (team != null) {
            text = team.getName();
        }

        if (stadium != null) {
            if (Utils.isNullOrEmpty(text)) {
                text = stadium.getName();
            } else {
                text += " . " + stadium.getName();
            }
        }

        if (Utils.isNullOrEmpty(text)) {
            text = null;
        }

        return text;
    }

    public String getCustomerNamePhone(Reservation reservation) {
        String name = reservation.getCustomerName();
        String phone = reservation.getCustomerPhone();

        String text = name;

        if (!Utils.isNullOrEmpty(phone)) {
            if (Utils.isNullOrEmpty(text)) {
                text = phone;
            } else {
                text += " - " + phone;
            }
        }

        if (Utils.isNullOrEmpty(text)) {
            text = null;
        }

        return text;
    }

    public String getPhone(Reservation reservation) {
        Team team = reservation.getReservationTeam();
        if (team != null && !Utils.isNullOrEmpty(team.getCaptainPhone())) {
            return team.getCaptainPhone();
        } else {
            return reservation.getCustomerPhone();
        }
    }

    public int getFirstFieldCapacity(List<Reservation> reservations) {
        try {
            return reservations.get(0).getField().getPlayerCapacity();
        } catch (Exception e) {
            return -1;
        }
    }
}
