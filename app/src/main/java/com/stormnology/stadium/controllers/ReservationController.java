package com.stormnology.stadium.controllers;

import android.content.Context;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
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

    public String getCustomerTeamName(Reservation reservation) {
        Team team = reservation.getReservationTeam();

        String text = "";
        if (!Utils.isNullOrEmpty(reservation.getCustomerName())) {
            text = reservation.getCustomerName();
        }

        if (team != null) {
            if (Utils.isNullOrEmpty(text)) {
                text = team.getName();
            } else if (!Utils.isNullOrEmpty(team.getName())) {
                text += " . " + team.getName();
            }
        }

        if (Utils.isNullOrEmpty(text)) {
            text = null;
        }

        return text;
    }

    public String getShareableText(Context context, Reservation reservation) {
        // prepare stadium and field info
        String stadiumName = reservation.getReservationStadium() != null ?
                reservation.getReservationStadium().getName() : null;
        String fieldName = reservation.getField() != null ?
                reservation.getField().getFieldNumber() : null;

        // prepare times
        String timeFormat = "h:mm a";
        String startTime = DateUtils.formatDate(reservation.getTimeStart(), Const.SER_TIME_FORMAT, timeFormat);
        String endTime = DateUtils.formatDate(reservation.getTimeEnd(), Const.SER_TIME_FORMAT, timeFormat);

        // get app play store url
        String appUrl = Utils.getPlayStoreAppUrl(context);

        // prepare the text and return it
        String text = context.getString(R.string.share_reservation_text,
                reservation.getDayName(), stadiumName, fieldName, startTime, endTime, appUrl);
        return text;
    }
}
