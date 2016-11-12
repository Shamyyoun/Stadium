package com.stadium.app.models.entities;

import com.stadium.app.Const;
import com.stadium.app.models.Checkable;
import com.stadium.app.utils.DateUtils;

import java.io.Serializable;

public class Time implements Serializable, Checkable {
    private String start;
    private String end;
    private boolean checked;
    private String defaultName; // for display purposes. Used when no start or end to be displayed in the dialog list

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    @Override
    public String toString() {
        if (start != null && end != null) {
            // TODO should extract hardcoded strings to the strings file
            String start = DateUtils.formatDate(this.start, Const.SER_TIME_FORMAT, "hh:mm a");
            String end = DateUtils.formatDate(this.end, Const.SER_TIME_FORMAT, "hh:mm a");
            String str = "من " + start + "  -  الى " + end;
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
