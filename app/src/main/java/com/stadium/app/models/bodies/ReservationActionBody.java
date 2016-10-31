package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Reservation;

public class ReservationActionBody {
    @SerializedName("captain")
    @Expose
    private CaptainBody captain;
    @SerializedName("reservation")
    @Expose
    private Reservation reservation;

    /**
     * @return The captain
     */
    public CaptainBody getCaptain() {
        return captain;
    }

    /**
     * @param captain The captain
     */
    public void setCaptain(CaptainBody captain) {
        this.captain = captain;
    }

    /**
     * @return The reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @param reservation The reservation
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}