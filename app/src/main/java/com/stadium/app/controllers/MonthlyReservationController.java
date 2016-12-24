package com.stadium.app.controllers;

import android.content.Context;

import com.stadium.app.models.enums.MonthlyReservationStatusType;

/**
 * Created by Shamyyoun on 12/24/16.
 */

public class MonthlyReservationController {

    public MonthlyReservationStatusType getStatusType(Context context, String status) {
        if (MonthlyReservationStatusType.AVAILABLE.getValue().equals(status)) {
            return MonthlyReservationStatusType.AVAILABLE;
        } else if (MonthlyReservationStatusType.CHANGE_FIELD.getValue().equals(status)) {
            return MonthlyReservationStatusType.CHANGE_FIELD;
        } else {
            return MonthlyReservationStatusType.NOT_AVAILABLE;
        }
    }
}
