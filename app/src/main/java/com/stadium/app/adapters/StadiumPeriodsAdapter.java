package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.UserController;
import com.stadium.app.dialogs.AdminAddReservationDialog;
import com.stadium.app.dialogs.ChooseFromCaptainTeamsDialog;
import com.stadium.app.dialogs.ReservationPlayersDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.interfaces.OnReservationAddedListener;
import com.stadium.app.interfaces.OnReservationPlayersSelectedListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.entities.Field;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class StadiumPeriodsAdapter extends ParentRecyclerAdapter<Reservation> {
    private static final String DISPLAY_TIME_FORMAT = "hh:mm a";

    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    private ActiveUserController activeUserController;
    private Reservation reservation; // this is just to hold data like stadium, field size and date.
    private boolean isAdminStadiumScreen; // this flag is used to notify the adapter to handle suitable item click
    private ChooseFromCaptainTeamsDialog teamsDialog;
    private ReservationPlayersDialog playersDialog;
    private OnReservationAddedListener reservationAddedListener;

    public StadiumPeriodsAdapter(Context context, List<Reservation> data, int layoutId, Reservation reservation) {
        super(context, data, layoutId);
        this.reservation = reservation;
        activeUserController = new ActiveUserController(context);
    }

    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ParentRecyclerViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        final Reservation item = data.get(position);

        // set field number
        Field field = item.getField();
        if (field != null) {
            holder.tvFieldNo.setText(field.getFieldNumber());
        } else {
            holder.tvFieldNo.setText("--");
        }

        // set period
        String timeStart = DateUtils.formatDate(item.getTimeStart(), Const.SER_TIME_FORMAT, DISPLAY_TIME_FORMAT);
        String timeEnd = DateUtils.formatDate(item.getTimeEnd(), Const.SER_TIME_FORMAT, DISPLAY_TIME_FORMAT);
        String period = timeStart + " " + getString(R.string.to) + " " + timeEnd;
        holder.tvPeriod.setText(period);

        // check layout content to add item click listener if possible
        if (holder.layoutContent != null) {
            // add item click listener
            holder.layoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(position);
                }
            });
        }
    }

    private void onItemClick(int position) {
        // check if admin stadium screen
        if (isAdminStadiumScreen) {
            showAdminAddReservationDialog(position);
        } else if (selectedTeam != null) { // check selected team
            // selected team exists
            // show choose players dialog
            choosePlayers(position, selectedTeam);
        } else {
            // no selected team
            // first, choose the team from teams dialog
            chooseTeam(position);
        }
    }

    private void showAdminAddReservationDialog(final int position) {
        Reservation reservation = data.get(position);
        AdminAddReservationDialog dialog = new AdminAddReservationDialog(context, reservation);
        dialog.setOnReservationAddedListener(new OnReservationAddedListener() {
            @Override
            public void onReservationAdded(Reservation reservation) {
                // remove item and fire the listener if possible
                removeItem(position);
                if (reservationAddedListener != null) {
                    reservationAddedListener.onReservationAdded(reservation);
                }
            }
        });

        dialog.show();
    }

    private void chooseTeam(final int position) {
        if (teamsDialog == null) {
            teamsDialog = new ChooseFromCaptainTeamsDialog(context);
            teamsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // get selected team and choose the players
                    Team team = (Team) item;
                    choosePlayers(position, team);
                }
            });
        }

        teamsDialog.show();
    }

    private void choosePlayers(final int position, final Team team) {
        playersDialog = new ReservationPlayersDialog(context, team.getId());
        playersDialog.setOnPlayersSelectedListener(new OnReservationPlayersSelectedListener() {
            @Override
            public void onPlayersSelected(List<User> selectedPlayers, int playersCount) {
                showReservationConfirmDialog(position, team, selectedPlayers, playersCount);
            }
        });
        playersDialog.show();
    }

    private void showReservationConfirmDialog(final int position, final Team team,
                                              final List<User> selectedUsers, final int playersCount) {
        int msgId = R.string.continue_and_request_reservation_with_these_details;
        DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeReservation(position, team, selectedUsers, playersCount);
            }
        }, null);
    }

    private void makeReservation(final int position, Team team, List<User> selectedUsers, int playersCount) {
        // get the reservation obj
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener<Reservation> connectionListener = new ConnectionListener<Reservation>() {
            @Override
            public void onSuccess(Reservation response, int statusCode, String tag) {
                hideProgressDialog();

                // check result
                if (statusCode == Const.SER_CODE_200) {
                    Utils.showShortToast(context, R.string.reservation_requested_successfully);
                    removeItem(position);

                    // fire the listener if possible
                    if (reservationAddedListener != null) {
                        reservationAddedListener.onReservationAdded(response);
                    }
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseError(context, response, R.string.failed_requesting_reservation);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_requesting_reservation);
            }
        };

        // prepare complex params
        User user = activeUserController.getUser();
        UserController userController = new UserController(null);
        List<Integer> playersIds = userController.getIds(selectedUsers);
        int fieldId = (reservation.getField() != null) ? reservation.getField().getId() : 0;

        // send request
        ConnectionHandler connectionHandler = ApiRequests.captainAddReservation(context, connectionListener,
                user.getId(), user.getToken(), team.getId(), team.getName(), playersIds,
                reservation.getIntrvalNum(), reservation.getPrice(), playersCount, fieldId,
                reservation.getDate(), reservation.getTimeStart(), reservation.getTimeEnd());
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private TextView tvFieldNo;
        private TextView tvPeriod;

        public ViewHolder(final View itemView) {
            super(itemView);
            layoutContent = findViewById(R.id.layout_content);
            tvFieldNo = (TextView) findViewById(R.id.tv_field_no);
            tvPeriod = (TextView) findViewById(R.id.tv_period);
        }
    }

    public void setSelectedTeam(Team selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    public void setOnReservationAddedListener(OnReservationAddedListener reservationAddedListener) {
        this.reservationAddedListener = reservationAddedListener;
    }

    public void setAdminStadiumScreen(boolean adminStadiumScreen) {
        isAdminStadiumScreen = adminStadiumScreen;
    }
}
