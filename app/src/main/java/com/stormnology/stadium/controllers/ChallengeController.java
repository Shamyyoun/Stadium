package com.stormnology.stadium.controllers;

import android.content.Context;

import com.stormnology.stadium.R;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 7/16/17.
 */

public class ChallengeController {

    public boolean isChallengeForAll(Challenge challenge) {
        Team guestTeam = challenge.getGuestTeam();
        if (guestTeam != null) {
            return guestTeam.getId() == 0;
        } else {
            return false;
        }
    }

    public String getPlaceInfo(Context context, Challenge challenge) {
        // get objects
        ReservationController reservationController = new ReservationController();
        Reservation reservation = challenge.getReservation();

        // check reservation to set place info
        String placeInfo;
        if (reservation != null) {
            // set place as stadium & field no.
            placeInfo = reservationController.getStadiumFieldName(context, reservation);
        } else {
            // set place as place name
            placeInfo = challenge.getPlace();
        }

        return placeInfo;
    }

    public String getDateInfo(Challenge challenge) {
        // get objects
        ReservationController reservationController = new ReservationController();
        Reservation reservation = challenge.getReservation();

        // check reservation to set date info
        String dateInfo = null;
        if (reservation != null) {
            // set date as day . date . time
            dateInfo = reservationController.getDayDateTime(reservation);
        } else {
            // prepare the date info
            if (!Utils.isNullOrEmpty(challenge.getDay())) {
                dateInfo = challenge.getDay();
            }

            if (!Utils.isNullOrEmpty(challenge.getTime())) {
                if (dateInfo != null) {
                    dateInfo += " . ";
                }

                dateInfo += challenge.getTime();
            }
        }

        return dateInfo;
    }

    public String getCreationDate(Challenge challenge, PrettyTime prettyTime) {
        // create pretty time obj if required
        if (prettyTime == null) {
            prettyTime = new PrettyTime(new Locale("ar"));
        }

        // check the date
        if (challenge.getDate() != null) {
            // prepare the suitable date
            final Calendar calendar = DateUtils.convertToCalendar(challenge.getDate(), Challenge.DATE_FORMAT);
            String date;
            if (DateUtils.isToday(calendar)) {
                date = prettyTime.format(calendar);
            } else {
                date = DateUtils.formatDate(challenge.getDate(), Challenge.DATE_FORMAT, "yyyy-M-d");
            }

            return date;
        } else {
            return null;
        }
    }

    public String getHostTeamName(Challenge challenge) {
        Team hostTeam = challenge.getHostTeam();
        if (!Utils.isNullOrEmpty(hostTeam.getName())) {
            return hostTeam.getName();
        } else {
            return "---------";
        }
    }

    public String getGuestTeamName(Context context, Challenge challenge) {
        Team guestTeam = challenge.getGuestTeam();
        if (isChallengeForAll(challenge)) {
            return context.getString(R.string.the_all);
        } else if (!Utils.isNullOrEmpty(guestTeam.getName())) {
            return guestTeam.getName();
        } else {
            return "---------";
        }
    }

    public boolean playerCanAcceptChallenge(int playerId, Challenge challenge) {
        // check if challenge is for all
        if (isChallengeForAll(challenge)) {
            return true;
        }

        // check if the user is captain in the guest team of the challenge
        TeamController teamController = new TeamController();
        Team guestTeam = challenge.getGuestTeam();
        if (teamController.isCaptain(guestTeam, playerId)) {
            return true;
        } else {
            return false;
        }
    }
}
