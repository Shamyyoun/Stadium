
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.responses.MonthlyReservationResponse;

public class AddMonthlyReservationBody {
    @SerializedName("user")
    @Expose
    private AdminBody admin;
    @SerializedName("reservation")
    @Expose
    private MonthlyReservationResponse reservation;

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
     * @return The reservation
     */
    public MonthlyReservationResponse getReservation() {
        return reservation;
    }

    /**
     * @param reservation The reservation
     */
    public void setReservation(MonthlyReservationResponse reservation) {
        this.reservation = reservation;
    }

}
