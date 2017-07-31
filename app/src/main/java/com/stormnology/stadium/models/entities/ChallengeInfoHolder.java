package com.stormnology.stadium.models.entities;

import java.io.Serializable;

/**
 * Created by Shamyyoun on 7/30/17.
 */

public class ChallengeInfoHolder implements Serializable {
    private Team hostTeam;
    private Team guestTeam;
    private ChallengeType type;
    private Reservation reservation;
    private Stadium place;
    private String day;
    private String time;

    public Team getHostTeam() {
        return hostTeam;
    }

    public void setHostTeam(Team hostTeam) {
        this.hostTeam = hostTeam;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(Team guestTeam) {
        this.guestTeam = guestTeam;
    }

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Stadium getPlace() {
        return place;
    }

    public void setPlace(Stadium place) {
        this.place = place;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
