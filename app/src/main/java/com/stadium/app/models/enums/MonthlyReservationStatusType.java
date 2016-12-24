package com.stadium.app.models.enums;

import android.content.Context;

import com.stadium.app.R;

/**
 * Created by Shamyyoun on 6/4/16.
 */
public enum MonthlyReservationStatusType {
    AVAILABLE("availble", R.string.available),
    NOT_AVAILABLE("no availble reservation", R.string.not_available_in_this_duration),
    CHANGE_FIELD("change field", R.string.available_but_change_field_or_more);

    private String value;
    private int titleResId;

    MonthlyReservationStatusType(String value, int titleResId) {
        this.value = value;
        this.titleResId = titleResId;
    }

    public String getValue() {
        return value;
    }

    public String getTitle(Context context) {
        return context.getString(titleResId);
    }
}
