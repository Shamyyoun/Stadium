package com.stormnology.stadium.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.dialogs.ChooseChallengeDayDialog;
import com.stormnology.stadium.dialogs.ChooseChallengeTimeDialog;
import com.stormnology.stadium.dialogs.ChooseFromCaptainTeamsDialog;
import com.stormnology.stadium.dialogs.ChooseFromChallengeStadiumDialog;
import com.stormnology.stadium.dialogs.ChooseFromChallengeTeamsDialog;
import com.stormnology.stadium.dialogs.ChooseReservationDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.ChallengeDay;
import com.stormnology.stadium.models.entities.ChallengeInfoHolder;
import com.stormnology.stadium.models.entities.ChallengeTime;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class AddChallengeActivity extends ParentActivity {
    private ChallengeInfoHolder infoHolder;
    private Button btnHostTeam;
    private Button btnGuestTeam;
    private EditText etComment;
    private Button btnReservation;
    private View layoutPlaceTimeInfo;
    private Button btnPlace;
    private Button btnDay;
    private Button btnTime;
    private Button btnAdd;

    private ChooseFromCaptainTeamsDialog hostTeamsDialog;
    private ChooseFromChallengeTeamsDialog guestTeamsDialog;
    private ChooseReservationDialog reservationDialog;
    private ChooseFromChallengeStadiumDialog stadiumsDialog;
    private ChooseChallengeDayDialog daysDialog;
    private ChooseChallengeTimeDialog timesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge);

        // customize the toolbar
        enableBackButton();

        // obtain main objects
        infoHolder = new ChallengeInfoHolder();

        // init views
        btnHostTeam = (Button) findViewById(R.id.btn_host_team);
        btnGuestTeam = (Button) findViewById(R.id.btn_guest_team);
        etComment = (EditText) findViewById(R.id.et_comment);
        btnReservation = (Button) findViewById(R.id.btn_reservation);
        layoutPlaceTimeInfo = findViewById(R.id.layout_place_time_info);
        btnPlace = (Button) findViewById(R.id.btn_place);
        btnDay = (Button) findViewById(R.id.btn_day);
        btnTime = (Button) findViewById(R.id.btn_time);
        btnAdd = (Button) findViewById(R.id.btn_add);

        // add listeners
        btnHostTeam.setOnClickListener(this);
        btnGuestTeam.setOnClickListener(this);
        btnReservation.setOnClickListener(this);
        btnPlace.setOnClickListener(this);
        btnDay.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    private void updateHostTeamUI() {
        if (infoHolder.getHostTeam() != null) {
            String str = getString(R.string.challenger_team) + ": " + infoHolder.getHostTeam().toString();
            btnHostTeam.setText(str);
        } else {
            btnHostTeam.setText(R.string.challenger_team);
        }
    }

    private void updateGuestTeamUI() {
        if (infoHolder.getGuestTeam() != null) {
            String str = getString(R.string.who_you_are_challenging) + ": " + infoHolder.getGuestTeam().toString();
            btnGuestTeam.setText(str);
        } else {
            btnGuestTeam.setText(R.string.who_you_are_challenging);
        }
    }

    private void updateReservationUI() {
        if (infoHolder.getReservation() != null) {
            String str = getString(R.string.reservation) + ": " + infoHolder.getReservation().toString();
            btnReservation.setText(str);
        } else {
            btnReservation.setText(R.string.reservation);
        }
    }

    private void updatePlaceUI() {
        if (infoHolder.getPlace() != null) {
            String str = getString(R.string.place) + ": " + infoHolder.getPlace().toString();
            btnPlace.setText(str);
        } else {
            btnPlace.setText(R.string.place);
        }
    }

    private void updateDayUI() {
        if (infoHolder.getDay() != null) {
            String str = getString(R.string.day) + ": " + infoHolder.getDay().toString();
            btnDay.setText(str);
        } else {
            btnDay.setText(R.string.day);
        }
    }

    private void updateTimeUI() {
        if (infoHolder.getTime() != null) {
            String str = getString(R.string.time) + ": " + infoHolder.getTime().toString();
            btnTime.setText(str);
        } else {
            btnTime.setText(R.string.time);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guest_team:
                chooseGuestTeam();
                break;

            case R.id.btn_host_team:
                chooseHostTeam();
                break;

            case R.id.btn_reservation:
                chooseReservation();
                break;

            case R.id.btn_place:
                choosePlace();
                break;

            case R.id.btn_day:
                chooseDay();
                break;

            case R.id.btn_time:
                chooseTime();
                break;

            case R.id.btn_add:
                add();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseHostTeam() {
        if (hostTeamsDialog == null) {
            hostTeamsDialog = new ChooseFromCaptainTeamsDialog(this);
            hostTeamsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // update host team
                    Team team = (Team) item;
                    infoHolder.setHostTeam(team);
                    updateHostTeamUI();

                    // remove reservation
                    infoHolder.setReservation(null);
                    updateReservationUI();

                    // and remove place time info and hide wrapper layout
                    removePlaceTimeInfo();
                    layoutPlaceTimeInfo.setVisibility(View.GONE);
                }
            });
        }

        // check to select item if possible
        if (infoHolder.getHostTeam() != null) {
            hostTeamsDialog.setSelectedItemId(infoHolder.getHostTeam().getId());
        }

        hostTeamsDialog.show();
    }

    private void chooseGuestTeam() {
        if (guestTeamsDialog == null) {
            guestTeamsDialog = new ChooseFromChallengeTeamsDialog(this);
            guestTeamsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // update guest team
                    Team team = (Team) item;
                    infoHolder.setGuestTeam(team);
                    updateGuestTeamUI();
                }
            });
        }

        // check to select item if possible
        if (infoHolder.getGuestTeam() != null) {
            guestTeamsDialog.setSelectedItemId(infoHolder.getGuestTeam().getId());
        }

        guestTeamsDialog.show();
    }

    private void chooseReservation() {
        // check selected team
        if (infoHolder.getHostTeam() == null) {
            // show msg
            Utils.showShortToast(this, R.string.choose_challenger_team_first);
            return;
        }

        // check reservations dialog
        if (reservationDialog == null) {
            reservationDialog = new ChooseReservationDialog(this, infoHolder.getHostTeam().getId());
            reservationDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // update reservation
                    Reservation reservation = (Reservation) item;
                    infoHolder.setReservation(reservation);
                    updateReservationUI();

                    // check if later option
                    if (reservation.getId() == Const.DEFAULT_SERVER_ITEM_ID) {
                        // show place time info wrapper layout
                        layoutPlaceTimeInfo.setVisibility(View.VISIBLE);
                    } else {
                        // remove place time info and hide wrapper layout
                        removePlaceTimeInfo();
                        layoutPlaceTimeInfo.setVisibility(View.GONE);
                    }
                }
            });
        }

        // check to select item if possible
        if (infoHolder.getReservation() != null) {
            reservationDialog.setSelectedItemId(infoHolder.getReservation().getId());
        }

        reservationDialog.show();
    }

    private void removePlaceTimeInfo() {
        infoHolder.setPlace(null);
        infoHolder.setDay(null);
        infoHolder.setTime(null);

        updatePlaceUI();
        updateDayUI();
        updateTimeUI();
    }

    private void choosePlace() {
        if (stadiumsDialog == null) {
            stadiumsDialog = new ChooseFromChallengeStadiumDialog(this);
            stadiumsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Stadium stadium = (Stadium) item;
                    infoHolder.setPlace(stadium);
                    updatePlaceUI();
                }
            });
        }

        // check to select item if possible
        if (infoHolder.getPlace() != null) {
            stadiumsDialog.setSelectedItemId(infoHolder.getPlace().getId());
        }

        stadiumsDialog.show();
    }

    private void chooseDay() {
        if (daysDialog == null) {
            daysDialog = new ChooseChallengeDayDialog(this);
            daysDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // update day
                    ChallengeDay challengeDay = (ChallengeDay) item;
                    infoHolder.setDay(challengeDay.getName());
                    updateDayUI();
                }
            });
        }

        // check to select item if possible
        if (infoHolder.getDay() != null) {
            daysDialog.setSelectedItem(infoHolder.getDay());
        }

        daysDialog.show();
    }

    private void chooseTime() {
        if (timesDialog == null) {
            timesDialog = new ChooseChallengeTimeDialog(this);
            timesDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // update time
                    ChallengeTime challengeTime = (ChallengeTime) item;
                    infoHolder.setTime(challengeTime.getName());
                    updateTimeUI();
                }
            });
        }

        // check to select item if possible
        if (infoHolder.getTime() != null) {
            timesDialog.setSelectedItem(infoHolder.getTime());
        }

        timesDialog.show();
    }

    private void add() {
        String comment = Utils.getText(etComment);
        hideKeyboard();

        // validate main inputs
        if (infoHolder.getHostTeam() == null) {
            Utils.showShortToast(this, R.string.please_choose_challenger_team);
            return;
        }
        if (infoHolder.getGuestTeam() == null) {
            Utils.showShortToast(this, R.string.please_choose_who_you_are_challenging);
            return;
        }
        if (infoHolder.getReservation() == null) {
            Utils.showShortToast(this, R.string.please_choose_reservation);
            return;
        }

        // validate place time info if reserve later
        boolean reserveLater = infoHolder.getReservation().getId() == Const.DEFAULT_SERVER_ITEM_ID;
        if (reserveLater && infoHolder.getPlace() == null) {
            Utils.showShortToast(this, R.string.please_choose_place);
            return;
        }
        if (reserveLater && infoHolder.getDay() == null) {
            Utils.showShortToast(this, R.string.please_choose_day);
            return;
        }
        if (reserveLater && infoHolder.getTime() == null) {
            Utils.showShortToast(this, R.string.please_choose_time);
            return;
        }

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // prepare objects
        ActiveUserController activeUserController = new ActiveUserController(this);
        User user = activeUserController.getUser();
        Team hostTeam = infoHolder.getHostTeam();
        Team guestTeam = infoHolder.getGuestTeam();
        Reservation reservation = infoHolder.getReservation();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.addChallenge(this, this, user.getId(), user.getToken(),
                hostTeam.getId(), hostTeam.getName(), guestTeam.getId(), guestTeam.getName(),
                comment, reservation.getId(), infoHolder.getPlace().getName(),
                infoHolder.getDay(), infoHolder.getTime());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        if (statusCode == Const.SER_CODE_200) {
            Utils.showShortToast(this, R.string.added_successfully);
            setResult(RESULT_OK);
            finish();
        } else {
            String msg = AppUtils.getResponseMsg(this, response, R.string.failed_adding_challenge);
            Utils.showLongToast(this, msg);
        }
    }
}
