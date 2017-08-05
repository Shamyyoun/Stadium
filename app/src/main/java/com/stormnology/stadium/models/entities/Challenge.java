
package com.stormnology.stadium.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.responses.ServerResponse;

import java.io.Serializable;

public class Challenge extends ServerResponse implements Serializable, Cloneable {
    public static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("guest_comment")
    @Expose
    private String guestComment;
    @SerializedName("guest_goals")
    @Expose
    private int guestGoals;
    @SerializedName("guest_team")
    @Expose
    private Team guestTeam;
    @SerializedName("host_comment")
    @Expose
    private String hostComment;
    @SerializedName("host_goals")
    @Expose
    private int hostGoals;
    @SerializedName("host_team")
    @Expose
    private Team hostTeam;
    @SerializedName("objection")
    @Expose
    private boolean objection;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("reservation")
    @Expose
    private Reservation reservation;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("type")
    @Expose
    private ChallengeType type;
    @SerializedName("captainRole")
    @Expose
    private boolean captainRole;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGuestComment() {
        return guestComment;
    }

    public void setGuestComment(String guestComment) {
        this.guestComment = guestComment;
    }

    public int getGuestGoals() {
        return guestGoals;
    }

    public void setGuestGoals(int guestGoals) {
        this.guestGoals = guestGoals;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(Team guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getHostComment() {
        return hostComment;
    }

    public void setHostComment(String hostComment) {
        this.hostComment = hostComment;
    }

    public int getHostGoals() {
        return hostGoals;
    }

    public void setHostGoals(int hostGoals) {
        this.hostGoals = hostGoals;
    }

    public Team getHostTeam() {
        return hostTeam;
    }

    public void setHostTeam(Team hostTeam) {
        this.hostTeam = hostTeam;
    }

    public boolean isObjection() {
        return objection;
    }

    public void setObjection(boolean objection) {
        this.objection = objection;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public boolean isCaptainRole() {
        return captainRole;
    }

    public void setCaptainRole(boolean captainRole) {
        this.captainRole = captainRole;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Challenge cloneObject() {
        return (Challenge) clone();
    }
}
