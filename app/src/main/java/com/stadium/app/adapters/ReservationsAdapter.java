package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.ReservationController;
import com.stadium.app.controllers.StadiumController;
import com.stadium.app.controllers.TeamController;
import com.stadium.app.dialogs.AttendanceDialog;
import com.stadium.app.models.entities.Reservation;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.enums.ReservationsType;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ReservationsAdapter extends ParentRecyclerAdapter<Reservation> {
    private StadiumController stadiumController;
    private ReservationController reservationController;
    private TeamController teamController;
    private ActiveUserController userController;
    private ReservationsType reservationsType;
    // the team players when the adapter is a team reservations
    // to check if the user is a player in this team or not and specify what he can do
    private List<User> teamPlayers;

    public ReservationsAdapter(Context context, List<Reservation> data, int layoutId) {
        super(context, data, layoutId);

        // create controllers
        stadiumController = new StadiumController();
        reservationController = new ReservationController();
        teamController = new TeamController();
        userController = new ActiveUserController(context);
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

        // obtain main objects
        final Reservation item = data.get(position);
        User user = userController.getUser();

        // prepare the stadium values
        String stadiumName = null;
        String stadiumAddress = null;
        String stadiumImage = null;
        final Stadium stadium = item.getReservationStadium();
        if (stadium != null) {
            stadiumName = stadium.getName();
            stadiumAddress = stadiumController.getAddress(stadium);
            stadiumImage = stadium.getImageLink();
        }

        // prepare the name
        String name;
        if (reservationsType == ReservationsType.TEAM_RESERVATIONS) {
            if (Utils.isNullOrEmpty(stadiumName)) {
                name = null;
            } else {
                name = stadiumName;
            }
        } else {
            name = reservationController.getTeamStadiumName(item);
        }

        // set the name
        if (!Utils.isNullOrEmpty(name)) {
            holder.tvName.setText(name);
        } else {
            holder.tvName.setText("-----------");
        }

        // set the stadium address
        if (!Utils.isNullOrEmpty(stadiumAddress)) {
            holder.tvStadiumAddress.setText(getString(R.string.address_c) + " " + stadiumAddress);
            holder.tvStadiumAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvStadiumAddress.setVisibility(View.GONE);
        }

        // load the suitable image
        String image = null;
        if (reservationsType == ReservationsType.TEAM_RESERVATIONS) {
            image = stadiumImage;
        } else {
            if (item.getReservationTeam() != null) {
                image = item.getReservationTeam().getImageLink();
            }
        }
        Utils.loadImage(context, image, R.drawable.default_image, holder.ivImage);

        // set the field number
        String fieldNo = reservationController.getFieldNumber(item);
        if (fieldNo != null) {
            fieldNo = getString(R.string.stadium_name_c) + " " + fieldNo;
        } else {
            fieldNo = getString(R.string.stadium_name_c) + " ----";
        }
        holder.tvFieldNo.setText(fieldNo);

        // set the date
        String dateTime = getString(R.string.appointment_c) + " " + reservationController.getDateTime(item);
        holder.tvDateTime.setText(dateTime);

        // check to set the reservations count
        if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS) {
            String count = teamController.getReservationAbsentTotalCount(item.getReservationTeam());
            holder.tvReservationsCount.setText(count);
            holder.tvReservationsCount.setVisibility(View.VISIBLE);
        }

        // check to show / hide the buttons layout
        if (reservationsType == ReservationsType.ADMIN_TODAY_RESERVATIONS ||
                reservationsType == ReservationsType.ADMIN_ACCEPTED_RESERVATIONS) {
            // hide buttons layout
            holder.layoutButtons.setVisibility(View.GONE);
        } else {
            // check his role in the team to show / hide the cancel button
            Team team = item.getReservationTeam();
            if (team == null || !(teamController.isCaptain(team, user.getId())
                    || teamController.isAssistant(team, user.getId()))) {
                holder.layoutButtons.setVisibility(View.GONE);
            }
        }

        // check reservations type to customize the item clickable or not
        if (reservationsType == ReservationsType.ADMIN_TODAY_RESERVATIONS ||
                reservationsType == ReservationsType.ADMIN_ACCEPTED_RESERVATIONS ||
                reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS ||
                reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS) {
            // make the item not clickable
            holder.layoutContent.setEnabled(false);
            holder.ivIndicator.setVisibility(View.GONE);
        } else if (reservationsType == ReservationsType.TEAM_RESERVATIONS && teamPlayers != null) {
            // check if the user is a player in this team
            if (teamController.isTeamPlayer(teamPlayers, user.getId())) {
                // show the indicator
                holder.ivIndicator.setVisibility(View.VISIBLE);

                // check if the reservation is waiting to customize views
                if (Reservation.CONFIRM_WAITING.equals(item.getConfirm())) {
                    holder.layoutContent.setEnabled(false);
                    holder.ivIndicator.setImageResource(R.drawable.warning_icon);
                } else {
                    holder.layoutContent.setEnabled(true);
                    holder.ivIndicator.setImageResource(R.drawable.arrow_left);
                }
            } else {
                // he is not a player, so make the content layout not clickable
                // to prevent him from clicking and opening the attendance dialog
                holder.ivIndicator.setVisibility(View.GONE);
                holder.layoutContent.setEnabled(false);
            }
        }

        // create the global click listener
        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_content:
                        openAttendanceDialog(position);
                        break;

                    case R.id.btn_cancel:
                        showCancelConfirmDialog(position);
                        break;
                }
            }
        };

        // add listeners
        holder.layoutContent.setOnClickListener(clickListener);
        holder.btnCancel.setOnClickListener(clickListener);
    }

    private void openAttendanceDialog(int position) {
        Reservation reservation = data.get(position);
        AttendanceDialog dialog = new AttendanceDialog(context, reservation.getId());
        dialog.show();
    }

    private void showCancelConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.cancel_reservation_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(position);
            }
        }, null);
    }

    private void cancel(final int position) {
        // get the reservation
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener<String> connectionListener = new ConnectionListener<String>() {
            @Override
            public void onSuccess(String response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    removeItem(position);

                    // show msg
                    Utils.showShortToast(context, R.string.cancelled_successfully);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_cancelling);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_cancelling);
            }
        };

        // send request
        User user = userController.getUser();
        Team team = reservation.getReservationTeam();
        ConnectionHandler connectionHandler = ApiRequests.deleteReservation(context, connectionListener,
                user.getId(), user.getToken(), team.getId(), team.getName(), reservation.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivIndicator;
        private ImageView ivImage;
        private TextView tvReservationsCount;
        private TextView tvName;
        private TextView tvStadiumAddress;
        private TextView tvFieldNo;
        private TextView tvDateTime;
        private View layoutButtons;
        private Button btnCancel;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivIndicator = (ImageView) findViewById(R.id.iv_indicator);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvReservationsCount = (TextView) findViewById(R.id.tv_reservations_count);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvStadiumAddress = (TextView) findViewById(R.id.tv_stadium_address);
            tvFieldNo = (TextView) findViewById(R.id.tv_field_no);
            tvDateTime = (TextView) findViewById(R.id.tv_date_time);
            layoutButtons = findViewById(R.id.layout_buttons);
            btnCancel = (Button) findViewById(R.id.btn_cancel);
        }
    }

    public void setReservationsType(ReservationsType reservationsType) {
        this.reservationsType = reservationsType;
    }

    public void updateTeamPlayers(List<User> teamPlayers) {
        this.teamPlayers = teamPlayers;
        notifyDataSetChanged();
    }
}
