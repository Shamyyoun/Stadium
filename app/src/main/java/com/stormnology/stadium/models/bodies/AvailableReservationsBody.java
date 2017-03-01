
package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Stadium;

public class AvailableReservationsBody {

    @SerializedName("stadium")
    @Expose
    private Stadium stadium;
    @SerializedName("reservation")
    @Expose
    private Reservation reservation;

    /**
     * @return The stadium
     */
    public Stadium getStadium() {
        return stadium;
    }

    /**
     * @param stadium The stadium
     */
    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
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
