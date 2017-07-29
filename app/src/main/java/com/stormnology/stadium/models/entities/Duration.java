
package com.stormnology.stadium.models.entities;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.StadiumApp;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.utils.DateUtils;

import java.io.Serializable;

public class Duration implements Serializable, Checkable {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("dateId")
    @Expose
    private int dateId;
    @SerializedName("durationNumber")
    @Expose
    private int durationNumber;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    private boolean checked;
    private String defaultName; // for display purposes. Used when no start or end to be displayed in the dialog list

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

    public int getDurationNumber() {
        return durationNumber;
    }

    public void setDurationNumber(int durationNumber) {
        this.durationNumber = durationNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    @Override
    public String toString() {
        // get the context
        Context context = StadiumApp.getContext();

        // check start time and end time
        if (startTime != null && endTime != null) {
            String start = DateUtils.formatDate(startTime, Const.SER_TIME_FORMAT, "hh:mm a");
            String end = DateUtils.formatDate(endTime, Const.SER_TIME_FORMAT, "hh:mm a");
            String str = context.getString(R.string.from) + " " + start
                    + context.getString(R.string.to) + " - " + end;
            return str;
        } else {
            if (defaultName != null) {
                return defaultName;
            } else {
                return super.toString();
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

}
