package com.stormnology.stadium.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.ReservationController;
import com.stormnology.stadium.controllers.StadiumController;
import com.stormnology.stadium.controllers.TeamController;
import com.stormnology.stadium.dialogs.AttendanceDialog;
import com.stormnology.stadium.models.entities.Field;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ReservationConfirmType;
import com.stormnology.stadium.models.enums.ReservationsType;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

import static com.stormnology.stadium.R.string.confirm;

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
        if (Utils.isNullOrEmpty(stadiumName)) {
            stadiumName = "-----------";
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
            // set name as team . stadium name
            name = reservationController.getTeamStadiumName(item);

            // check it
            if (Utils.isNullOrEmpty(name)) {
                // set name as customer name - customer phone
                name = reservationController.getCustomerNamePhone(item);
            }
        }

        // prepare the field number
        String fieldNo = reservationController.getFieldNumber(item);
        if (Utils.isNullOrEmpty(fieldNo)) {
            fieldNo = "----";
        }

        // prepare the date time
        String dateTime = reservationController.getDateTime(item);

        // name is shared between all types, set it first :D
        if (!Utils.isNullOrEmpty(name)) {
            holder.tvName.setText(name);
        } else {
            holder.tvName.setText("-----------");
        }

        // date is shared to, set it
        String dateTimeStr = getString(R.string.appointment_c) + " " + dateTime;
        holder.tvDateTime.setText(dateTimeStr);

        // check reservations type to set basic data
        if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS
                || reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS
                || reservationsType == ReservationsType.ADMIN_MY_RESERVATIONS) {

            // this is a detailed view reservation
            // set field no msg
            String msg = getString(R.string.has_reserved_field) + " " + fieldNo;
            holder.tvFieldNoMsg.setText(msg);

            // set the reservations count and block count if it is new reservations
            if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS) {
                // set reservations count
                String count = teamController.getReservationAbsentTotalCount(item.getReservationTeam());
                holder.tvReservationsCount.setText(count);
                holder.tvReservationsCount.setVisibility(View.VISIBLE);

                // set block count
                int blockCount = 0;
                if (item.getReservationTeam() != null) {
                    blockCount = item.getReservationTeam().getBlockTimes();
                }
                holder.tvBlockCount.setText(blockCount + "/");
                holder.layoutBlockCount.setVisibility(View.VISIBLE);
            } else {
                // hide them
                holder.tvReservationsCount.setVisibility(View.GONE);
                holder.layoutBlockCount.setVisibility(View.GONE);
            }
        } else {
            // this is a simple view reservation
            // set the stadium address
            if (!Utils.isNullOrEmpty(stadiumAddress)) {
                holder.tvStadiumAddress.setText(getString(R.string.address_c) + " " + stadiumAddress);
                holder.tvStadiumAddress.setVisibility(View.VISIBLE);
            } else {
                holder.tvStadiumAddress.setVisibility(View.GONE);
            }

            // set the field number
            String filedNoStr = getString(R.string.stadium_name_c) + " " + fieldNo;
            holder.tvFieldNo.setText(filedNoStr);
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

        // check to show / hide the buttons layout
        if (reservationsType == ReservationsType.PLAYER_RESERVATIONS
                || reservationsType == ReservationsType.TEAM_RESERVATIONS) {

            // check player role in the team to show / hide the cancel button
            Team team = item.getReservationTeam();
            if (team != null && (teamController.isCaptain(team, user.getId())
                    || teamController.isAssistant(team, user.getId()))) {
                holder.layoutButtons.setVisibility(View.VISIBLE);
            } else {
                holder.layoutButtons.setVisibility(View.GONE);
            }
        } else {
            // this admin reservations adapter
            // show buttons layout
            holder.layoutButtons.setVisibility(View.VISIBLE);
        }

        // check reservations type to customize clickable root and action iv
        if (reservationsType == ReservationsType.TEAM_RESERVATIONS && teamPlayers != null) {
            // this is team reservations
            // check if the user is a player in this team
            if (teamController.isTeamPlayer(teamPlayers, user.getId())) {
                // show arrow indicator
                holder.ivAction.setImageResource(R.drawable.arrow_left);
                holder.ivAction.setEnabled(false);
                holder.ivAction.setVisibility(View.VISIBLE);

                // check if the reservation is waiting to customize views
                if (Reservation.CONFIRM_WAITING.equals(item.getConfirm())) {
                    holder.layoutContent.setEnabled(false);
                    holder.ivAction.setImageResource(R.drawable.warning_icon);
                } else {
                    holder.layoutContent.setEnabled(true);
                    holder.ivAction.setImageResource(R.drawable.arrow_left);
                }
            } else {
                // he is not a player, so make the content layout not clickable
                // to prevent him from clicking and opening the attendance dialog
                holder.ivAction.setVisibility(View.GONE);
                holder.layoutContent.setEnabled(false);
            }
        } else if (reservationsType == ReservationsType.PLAYER_RESERVATIONS) {
            // this is normal player reservations
            // show arrow indicator
            holder.ivAction.setImageResource(R.drawable.arrow_left);
            holder.ivAction.setEnabled(false);
            holder.ivAction.setVisibility(View.VISIBLE);
        } else {
            // this is admin reservations
            // make the item not clickable
            holder.layoutContent.setEnabled(false);
        }

        // create the global click listener
        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_content:
                        openAttendanceDialog(position);
                        break;

                    case R.id.iv_action:
                        openPhoneIntent(position);
                        break;

                    case R.id.btn_action1:
                        onAction1(position);
                        break;

                    case R.id.btn_action2:
                        onAction2(position);
                        break;

                    case R.id.btn_action3:
                        onAction3(position);
                        break;
                }
            }
        };

        // add click listeners
        addClickListener(holder.layoutContent, clickListener);
        addClickListener(holder.ivAction, clickListener);
        addClickListener(holder.btnAction1, clickListener);
        addClickListener(holder.btnAction2, clickListener);
        addClickListener(holder.btnAction3, clickListener);

        // customize buttons according to reservations type
        if (reservationsType == ReservationsType.ADMIN_TODAY_RESERVATIONS
                || reservationsType == ReservationsType.ADMIN_ACCEPTED_RESERVATIONS) {
            holder.btnAction1.setTextColor(getResColor(R.color.dark_gray));
            holder.btnAction1.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.refuse_icon, 0, 0, 0);
            holder.btnAction1.setVisibility(View.VISIBLE);
        } else if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS) {
            holder.btnAction1.setText(confirm);
            holder.btnAction2.setText(R.string.refuse);
            holder.btnAction3.setVisibility(View.GONE);
            holder.viewDivider2.setVisibility(View.GONE);
        } else if (reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS) {
            holder.btnAction1.setText(R.string.didnt_attend);
            holder.btnAction2.setText(R.string.block_team);
            holder.btnAction2.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.confirm_icon, 0, 0, 0);
            holder.btnAction3.setText(R.string.both);
        } else if (reservationsType == ReservationsType.ADMIN_MY_RESERVATIONS) {
            holder.btnAction2.setText(R.string.cancel_reservation);
            holder.btnAction1.setVisibility(View.GONE);
            holder.btnAction3.setVisibility(View.GONE);
            holder.viewDivider1.setVisibility(View.GONE);
            holder.viewDivider2.setVisibility(View.GONE);
        }
    }

    private void openAttendanceDialog(int position) {
        Reservation reservation = data.get(position);
        AttendanceDialog dialog = new AttendanceDialog(context, reservation);
        dialog.show();
    }

    private void openPhoneIntent(int position) {
        // get suitable phone
        Reservation reservation = data.get(position);
        String phone = reservationController.getPhone(reservation);

        // check phone
        if (!Utils.isNullOrEmpty(phone)) {
            Utils.openPhoneIntent(context, phone);
        } else {
            Utils.showShortToast(context, R.string.no_available_phone_for_this_reservation);
        }
    }

    private void onAction1(int position) {
        // check reservations type to call suitable method
        if (reservationsType == ReservationsType.ADMIN_TODAY_RESERVATIONS
                || reservationsType == ReservationsType.ADMIN_ACCEPTED_RESERVATIONS) {
            showCancelAdminResConfirmDialog(position);
        } else if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS) {
            showConfirmConfirmDialog(position, true);
        } else if (reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS) {
            showDidntAttendConfirmDialog(position);
        } else {
            showCancelConfirmDialog(position);
        }
    }

    private void onAction2(int position) {
        // check reservations type to call suitable method
        if (reservationsType == ReservationsType.ADMIN_NEW_RESERVATIONS) {
            showConfirmConfirmDialog(position, false);
        } else if (reservationsType == ReservationsType.ADMIN_PREVIOUS_RESERVATIONS) {
            showBlockTeamConfirmDialog(position);
        } else if (reservationsType == ReservationsType.ADMIN_MY_RESERVATIONS) {
            showCancelAdminResConfirmDialog(position);
        }
    }

    private void onAction3(int position) {
        showDidntAttendAndBlockConfirmDialog(position);
    }

    private void showConfirmConfirmDialog(final int position, final boolean confirm) {
        // prepare msg id
        int msgId = confirm ? R.string.confirm_this_reservation_q : R.string.decline_this_reservation_q;

        // show confirm dialog
        DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmReservation(position, confirm);
            }
        }, null);
    }

    private void showDidntAttendConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.report_this_team_didnt_attend_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportDidntAttend(position);
            }
        }, null);
    }

    private void showCancelConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.cancel_reservation_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancel(position);
            }
        }, null);
    }

    private void showCancelAdminResConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.cancel_reservation_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAdminRes(position);
            }
        }, null);
    }

    private void showBlockTeamConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.block_this_team_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blockTeam(position);
            }
        }, null);
    }

    private void showDidntAttendAndBlockConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.report_didnt_attend_and_block_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportDidntAttendAndBlock(position);
            }
        }, null);
    }

    private void confirmReservation(final int position, final boolean confirm) {
        // get the reservation
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check the status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg and remove it
                    Utils.showShortToast(context, confirm ? R.string.confirmed : R.string.refused);
                    removeItem(position);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, confirm ? R.string.error_confirming : R.string.error_declining);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, confirm ? R.string.error_confirming : R.string.error_declining);
            }
        };

        // prepare request params
        User user = userController.getUser();
        Stadium stadium = user.getAdminStadium();
        Team resetvationTeam = reservation.getReservationTeam();
        final int confirmType = confirm ? ReservationConfirmType.CONFIRM.getValue()
                : ReservationConfirmType.DECLINE.getValue();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.confirmReservation(context, listener,
                user.getId(), user.getToken(), stadium.getId(), stadium.getName(),
                reservation.getId(), resetvationTeam.getId(), confirmType);
        cancelWhenDestroyed(connectionHandler);
    }

    private void reportDidntAttend(final int position) {
        // get the reservation
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg and remove it
                    Utils.showShortToast(context, R.string.reported_successfully);
                    removeItem(position);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_reporting);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_reporting);
            }
        };

        // prepare request params
        User user = userController.getUser();
        Stadium stadium = user.getAdminStadium();
        Team reservationTeam = reservation.getReservationTeam();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.absentReservation(context, listener,
                user.getId(), user.getToken(), stadium.getId(), stadium.getName(),
                reservation.getId(), reservationTeam.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    private void blockTeam(final int position) {
        // get the reservation
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg and remove it
                    Utils.showShortToast(context, R.string.blocked_successfully);
                    removeItem(position);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_blocking);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_blocking);
            }
        };

        // prepare request params
        User user = userController.getUser();
        Stadium stadium = user.getAdminStadium();
        Team reservationTeam = reservation.getReservationTeam();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.blockTeam(context, listener,
                user.getId(), user.getToken(), stadium.getId(), stadium.getName(),
                reservation.getId(), reservationTeam.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    private void reportDidntAttendAndBlock(final int position) {
        // get the reservation
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg and remove it
                    Utils.showShortToast(context, R.string.reported_and_blocked_successfully);
                    removeItem(position);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_reporting_and_blocking);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_reporting_and_blocking);
            }
        };

        // prepare request params
        User user = userController.getUser();
        Stadium stadium = user.getAdminStadium();
        Team reservationTeam = reservation.getReservationTeam();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.absentBlockReservation(context, listener,
                user.getId(), user.getToken(), stadium.getId(), stadium.getName(),
                reservation.getId(), reservationTeam.getId());
        cancelWhenDestroyed(connectionHandler);
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
        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
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

        // prepare params
        User user = userController.getUser();
        Team team = reservation.getReservationTeam();
        Field field = reservation.getField();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.deleteReservation(context, listener,
                user.getId(), user.getToken(), team.getId(), team.getName(),
                reservation.getId(), field.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    private void cancelAdminRes(final int position) {
        // get the reservation
        final Reservation reservation = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg and remove it
                    Utils.showShortToast(context, R.string.cancelled_successfully);
                    removeItem(position);
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

        // prepare request params
        User user = userController.getUser();
        Stadium stadium = user.getAdminStadium();
        Team reservationTeam = reservation.getReservationTeam();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.cancelReservation(context, listener,
                user.getId(), user.getToken(), stadium.getId(), reservation.getId(),
                reservationTeam.getId(), reservationTeam.getName());
        cancelWhenDestroyed(connectionHandler);
    }

    private void addClickListener(View view, View.OnClickListener clickListener) {
        if (view != null) {
            view.setOnClickListener(clickListener);
        }
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivAction;
        private ImageView ivImage;
        private TextView tvReservationsCount;
        private TextView tvName;
        private TextView tvStadiumAddress;
        private TextView tvFieldNo;
        private TextView tvDateTime;
        private TextView tvFieldNoMsg;
        private View layoutBlockCount;
        private TextView tvBlockCount;
        private View layoutButtons;
        private Button btnAction1;
        private Button btnAction2;
        private Button btnAction3;
        private View viewDivider1;
        private View viewDivider2;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivAction = (ImageView) findViewById(R.id.iv_action);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvReservationsCount = (TextView) findViewById(R.id.tv_reservations_count);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvStadiumAddress = (TextView) findViewById(R.id.tv_stadium_address);
            tvFieldNo = (TextView) findViewById(R.id.tv_field_no);
            tvDateTime = (TextView) findViewById(R.id.tv_date_time);
            tvFieldNoMsg = (TextView) findViewById(R.id.tv_field_no_msg);
            layoutBlockCount = findViewById(R.id.layout_block_count);
            tvBlockCount = (TextView) findViewById(R.id.tv_block_count);
            layoutButtons = findViewById(R.id.layout_buttons);
            btnAction1 = (Button) findViewById(R.id.btn_action1);
            btnAction2 = (Button) findViewById(R.id.btn_action2);
            btnAction3 = (Button) findViewById(R.id.btn_action3);
            viewDivider1 = findViewById(R.id.view_divider1);
            viewDivider2 = findViewById(R.id.view_divider2);
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
