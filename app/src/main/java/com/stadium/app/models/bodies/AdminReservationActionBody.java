
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Reservation;

public class AdminReservationActionBody {
    @SerializedName("reservation")
    @Expose
    private Reservation reservation;
    @SerializedName("admin")
    @Expose
    private AdminBody admin;
    @SerializedName("user")
    @Expose
    private AdminBody user;
    @SerializedName("b")
    @Expose
    private int b;

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

    /**
     * @return The admin
     */
    public AdminBody getAdmin() {
        return admin;
    }

    /**
     * @param admin The admin
     */
    public void setAdmin(AdminBody admin) {
        this.admin = admin;
    }

    /**
     * @return The b
     */
    public int getB() {
        return b;
    }

    /**
     * @param b The b
     */
    public void setB(int b) {
        this.b = b;
    }

    /**
     * @return The user
     */
    public AdminBody getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(AdminBody user) {
        this.user = user;
    }
}
