package com.stormnology.stadium.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.dialogs.ChooseDayDialog;
import com.stormnology.stadium.dialogs.ChooseFieldDialog;
import com.stormnology.stadium.dialogs.ChooseFromAllTeamsDialog;
import com.stormnology.stadium.dialogs.ChooseFromMyDurationsDialog;
import com.stormnology.stadium.dialogs.MonthlyReservationDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.interfaces.OnReservationAddedListener;
import com.stormnology.stadium.interfaces.OnTeamSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.Day;
import com.stormnology.stadium.models.entities.Duration;
import com.stormnology.stadium.models.entities.Field;
import com.stormnology.stadium.models.entities.RepeatedReservation;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.responses.MonthlyReservationResponse;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DatePickerFragment;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.Calendar;

/**
 * Created by karam on 8/10/16.
 */
public class RepeatedReservationFragment extends ParentFragment implements OnReservationAddedListener {
    private static final String DISPLAYED_DATE_FORMAT = "d-M-yyyy";

    private Button btnTeamName;
    private Button btnFieldNo;
    private EditText etPrice;
    private Button btnDateFrom;
    private Button btnDateTo;
    private Button btnDuration;
    private Button btnDay;
    private Button btnAdd;

    private RepeatedReservation holder; // used to hold objects & values
    private ChooseFromAllTeamsDialog teamsDialog;
    private ChooseFieldDialog fieldDialog;
    private DatePickerFragment datePickerFragment;
    private ChooseFromMyDurationsDialog durationsDialog;
    private ChooseDayDialog daysDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.repeated_reservation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_repeated_reservation, container, false);

        // create the holder object
        holder = new RepeatedReservation();

        // init views
        btnTeamName = (Button) findViewById(R.id.btn_team_name);
        btnFieldNo = (Button) findViewById(R.id.btn_field_no);
        etPrice = (EditText) findViewById(R.id.et_price);
        btnDateFrom = (Button) findViewById(R.id.btn_date_from);
        btnDateTo = (Button) findViewById(R.id.btn_date_to);
        btnDuration = (Button) findViewById(R.id.btn_duration);
        btnDay = (Button) findViewById(R.id.btn_day);
        btnAdd = (Button) findViewById(R.id.btn_add);

        // add listeners
        btnTeamName.setOnClickListener(this);
        btnFieldNo.setOnClickListener(this);
        btnDateFrom.setOnClickListener(this);
        btnDateTo.setOnClickListener(this);
        btnDuration.setOnClickListener(this);
        btnDay.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_team_name:
                chooseTeam();
                break;

            case R.id.btn_field_no:
                chooseField();
                break;

            case R.id.btn_date_from:
                chooseFromDate();
                break;

            case R.id.btn_date_to:
                chooseToDate();
                break;

            case R.id.btn_duration:
                onDuration();
                break;

            case R.id.btn_day:
                chooseDay();
                break;

            case R.id.btn_add:
                add();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseTeam() {
        if (teamsDialog == null) {
            teamsDialog = new ChooseFromAllTeamsDialog(activity);
            teamsDialog.setOnTeamSelectedListener(new OnTeamSelectedListener() {
                @Override
                public void onTeamSelected(Team team) {
                    holder.setTeam(team);
                    updateTeamUI();
                }
            });
        }

        teamsDialog.show();
    }

    private void updateTeamUI() {
        if (holder.getTeam() != null) {
            String str = getString(R.string.the_team) + ": " + holder.getTeam().getName();
            btnTeamName.setText(str);
        } else {
            btnTeamName.setText(R.string.team_name);
        }
    }

    private void chooseField() {
        if (fieldDialog == null) {
            fieldDialog = new ChooseFieldDialog(activity);
            fieldDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Field field = (Field) item;
                    holder.setField(field);
                    updateFieldUI();
                }
            });
        }

        // select current field if possible
        if (holder.getField() != null) {
            fieldDialog.setSelectedItemId(holder.getField().getId());
        }

        fieldDialog.show();
    }

    private void updateFieldUI() {
        if (holder.getField() != null) {
            String str = getString(R.string.field_c) + " " + holder.getField().getFieldNumber();
            btnFieldNo.setText(str);
        } else {
            btnFieldNo.setText(R.string.field_number);
        }
    }

    private void createDatePickerFragment(Calendar minDate) {
        if (datePickerFragment == null) {
            datePickerFragment = new DatePickerFragment();
        }

        // set min date
        datePickerFragment.setMinDate(minDate);
    }

    private void chooseFromDate() {
        // create and customize the date picker fragment
        Calendar minDate = DateUtils.addDays(1);
        createDatePickerFragment(minDate);
        if (holder.getDateFrom() != null) {
            datePickerFragment.setDate(holder.getDateFrom(), Const.SER_DATE_FORMAT);
        }

        // add listener
        datePickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // format date, set it in the holder and update its ui
                String date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
                date = DateUtils.formatDate(date, DISPLAYED_DATE_FORMAT, Const.SER_DATE_FORMAT);
                holder.setDateFrom(date);
                updateFromDateUI();

                // check to date
                if (holder.getDateTo() != null) {
                    // compare the two dates
                    int compareResult = DateUtils.compare(holder.getDateTo(), holder.getDateFrom(), Const.SER_DATE_FORMAT);
                    if (compareResult != 1) {
                        // to date isn't higher than from date
                        // reset it
                        holder.setDateTo(null);
                        updateToDateUI();
                    }
                }

                // reset duration
                holder.setDuration(null);
                durationsDialog = null;
                updateDurationUI();
            }
        });

        // show date dialog
        datePickerFragment.show(activity.getSupportFragmentManager(), null);
    }

    private void updateFromDateUI() {
        if (holder.getDateFrom() != null) {
            String date = DateUtils.formatDate(holder.getDateFrom(), Const.SER_DATE_FORMAT, DISPLAYED_DATE_FORMAT);
            String str = getString(R.string.from) + ": " + date;
            btnDateFrom.setText(str);
        } else {
            btnDateFrom.setText(R.string.from);
        }
    }

    private void chooseToDate() {
        // prepare min date
        Calendar minDate;
        if (holder.getDateFrom() != null) {
            minDate = DateUtils.addDays(holder.getDateFrom(), Const.SER_DATE_FORMAT, 1);
        } else {
            minDate = DateUtils.addDays(2);
        }

        // create and customize the date picker fragment
        createDatePickerFragment(minDate);
        if (holder.getDateTo() != null) {
            datePickerFragment.setDate(holder.getDateTo(), Const.SER_DATE_FORMAT);
        }

        // add listener
        datePickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // format date, set it in the holder and update its ui
                String date = dayOfMonth + "-" + (++monthOfYear) + "-" + year;
                date = DateUtils.formatDate(date, DISPLAYED_DATE_FORMAT, Const.SER_DATE_FORMAT);
                holder.setDateTo(date);
                updateToDateUI();

                // reset duration
                holder.setDuration(null);
                durationsDialog = null;
                updateDurationUI();
            }
        });

        // show date dialog
        datePickerFragment.show(activity.getSupportFragmentManager(), null);
    }

    private void updateToDateUI() {
        if (holder.getDateTo() != null) {
            String date = DateUtils.formatDate(holder.getDateTo(), Const.SER_DATE_FORMAT, DISPLAYED_DATE_FORMAT);
            String str = getString(R.string.to) + ": " + date;
            btnDateTo.setText(str);
        } else {
            btnDateTo.setText(R.string.to);
        }
    }

    private void onDuration() {
        // check the two dates
        if (holder.getDateFrom() != null && holder.getDateTo() != null) {
            chooseDuration();
        } else {
            // show msg
            Utils.showShortToast(activity, R.string.please_choose_the_date_first);
        }
    }

    private void chooseDuration() {
        if (durationsDialog == null) {
            durationsDialog = new ChooseFromMyDurationsDialog(activity, holder.getDateFrom(), holder.getDateTo());
            durationsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Duration duration = (Duration) item;
                    holder.setDuration(duration);
                    updateDurationUI();
                }
            });
        }

        // select current duration if possible
        if (holder.getDuration() != null) {
            durationsDialog.setSelectedItemId(holder.getDuration().getId());
        }

        durationsDialog.show();
    }

    private void updateDurationUI() {
        if (holder.getDuration() != null) {
            String str = getString(R.string.time) + ": " + holder.getDuration().toString();
            btnDuration.setText(str);
        } else {
            btnDuration.setText(R.string.time);
        }
    }

    private void chooseDay() {
        if (daysDialog == null) {
            daysDialog = new ChooseDayDialog(activity);
            daysDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Day day = (Day) item;
                    holder.setDay(day);
                    updateDayUI();
                }
            });
        }

        // select current day if possible
        if (holder.getDay() != null) {
            daysDialog.setSelectedItemId(holder.getDay().getId());
        }

        daysDialog.show();
    }

    private void updateDayUI() {
        if (holder.getDay() != null) {
            String str = getString(R.string.day) + ": " + holder.getDay().getTitle();
            btnDay.setText(str);
        } else {
            btnDay.setText(R.string.day);
        }
    }

    private void add() {
        // add price to the holder
        String priceStr = Utils.getText(etPrice);
        float price = (float) Utils.convertToDouble(priceStr);
        holder.setPrice(price);

        // validate inputs
        if (holder.getTeam() == null
                || holder.getField() == null
                || Utils.isEmpty(etPrice)
                || holder.getDateFrom() == null
                || holder.getDateTo() == null
                || holder.getDuration() == null
                || holder.getDay() == null) {

            // show msg
            Utils.showShortToast(activity, R.string.please_fill_all_fields);
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // get stadium id
        ActiveUserController activeUserController = new ActiveUserController(activity);
        int stadiumId = activeUserController.getUser().getAdminStadium().getId();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.monthlyReservation(activity, this,
                stadiumId, holder.getDateFrom(), holder.getDateTo(), holder.getField().getId(),
                holder.getField().getFieldSize(), holder.getDuration().getDurationNumber(), holder.getDay().getValue());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check response
        MonthlyReservationResponse monthlyReservationResponse = (MonthlyReservationResponse) response;
        if (statusCode == Const.SER_CODE_200 && monthlyReservationResponse != null) {
            // show the reservation dialog
            MonthlyReservationDialog dialog = new MonthlyReservationDialog(activity, holder,
                    monthlyReservationResponse);
            dialog.setOnReservationAddedListener(this);
            dialog.show();
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(activity, response, R.string.failed_adding_reservation);
            Utils.showLongToast(activity, msg);
        }
    }

    @Override
    public void onReservationAdded(Reservation reservation) {
        reset();
    }

    private void reset() {
        holder = new RepeatedReservation();
        durationsDialog = null;
        updateUI();
    }

    private void updateUI() {
        updateTeamUI();
        updateFieldUI();
        etPrice.setText("");
        updateFromDateUI();
        updateToDateUI();
        updateDurationUI();
        updateDayUI();
    }
}