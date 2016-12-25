
package com.stadium.app.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Reservation;

import java.util.List;

public class MonthlyReservationResponse extends ServerResponse {

    @SerializedName("AvailbleRes")
    @Expose
    private List<Reservation> availableRes = null;
    @SerializedName("AvailbleReservations")
    @Expose
    private List<Reservation> availableReservations = null;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("FieldId")
    @Expose
    private int fieldId;
    @SerializedName("FieldSize")
    @Expose
    private String fieldSize;
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("countDay")
    @Expose
    private int countDay;
    @SerializedName("dayName")
    @Expose
    private String dayName;
    @SerializedName("intervalNum")
    @Expose
    private int intervalNum;
    @SerializedName("resDate")
    @Expose
    private Object resDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("stdiumId")
    @Expose
    private int stadiumId;
    @SerializedName("teamId")
    @Expose
    private int teamId;
    @SerializedName("price")
    @Expose
    private float price;

    public List<Reservation> getAvailableReservations() {
        return availableReservations;
    }

    public void setAvailableReservations(List<Reservation> availableReservations) {
        this.availableReservations = availableReservations;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(String fieldSize) {
        this.fieldSize = fieldSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getCountDay() {
        return countDay;
    }

    public void setCountDay(int countDay) {
        this.countDay = countDay;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public int getIntervalNum() {
        return intervalNum;
    }

    public void setIntervalNum(int intervalNum) {
        this.intervalNum = intervalNum;
    }

    public Object getResDate() {
        return resDate;
    }

    public void setResDate(Object resDate) {
        this.resDate = resDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(int stadiumId) {
        this.stadiumId = stadiumId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
