
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthlyReservationBody {

    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("FieldId")
    @Expose
    private int fieldId;
    @SerializedName("FieldSize")
    @Expose
    private String fieldSize;
    @SerializedName("dayName")
    @Expose
    private String dayName;
    @SerializedName("intervalNum")
    @Expose
    private int intervalNum;
    @SerializedName("stdiumId")
    @Expose
    private int stadiumId;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public int getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(int stadiumId) {
        this.stadiumId = stadiumId;
    }

}
