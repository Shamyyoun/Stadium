
package com.stormnology.stadium.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Duration;

import java.util.List;

public class DurationsResponse {

    @SerializedName("NextTimes")
    @Expose
    private List<Duration> nextTimes = null;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("stadiumId")
    @Expose
    private int stadiumId;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("times")
    @Expose
    private List<Duration> times = null;

    public List<Duration> getNextTimes() {
        return nextTimes;
    }

    public void setNextTimes(List<Duration> nextTimes) {
        this.nextTimes = nextTimes;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(int stadiumId) {
        this.stadiumId = stadiumId;
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
