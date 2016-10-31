
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.User;

public class PlayerConfirmListBody {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("reservation")
    @Expose
    private Reservation reservation;

    /**
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
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
