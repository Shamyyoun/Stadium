
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Duration;

import java.util.List;

public class ChangeDurationsBody {

    @SerializedName("admin")
    @Expose
    private AdminBody admin;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("times")
    @Expose
    private List<Duration> times = null;

    public AdminBody getAdmin() {
        return admin;
    }

    public void setAdmin(AdminBody admin) {
        this.admin = admin;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<Duration> getTimes() {
        return times;
    }

    public void setTimes(List<Duration> times) {
        this.times = times;
    }

}
