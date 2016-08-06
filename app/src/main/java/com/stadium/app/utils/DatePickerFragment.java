package com.stadium.app.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 9/11/2015.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (calendar == null) {
            // Use the current date as the default date in the picker
            calendar = Calendar.getInstance();
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    }


    public DatePickerDialog.OnDateSetListener getDatePickerListener() {
        return datePickerListener;
    }

    public void setDatePickerListener(DatePickerDialog.OnDateSetListener datePickerListener) {
        this.datePickerListener = datePickerListener;
    }

    public void setDate(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setDate(String date, String dateFormat) {
        if (date != null) {
            try {
                calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
                calendar.setTime(sdf.parse(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}