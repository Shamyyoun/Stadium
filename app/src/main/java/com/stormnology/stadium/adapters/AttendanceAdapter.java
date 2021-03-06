package com.stormnology.stadium.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.AttendanceController;
import com.stormnology.stadium.dialogs.ProgressDialog;
import com.stormnology.stadium.models.entities.Attendant;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ReservationConfirmType;
import com.stormnology.stadium.models.enums.ReservationStatusType;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class AttendanceAdapter extends ParentRecyclerAdapter<Attendant> {
    private Reservation reservation;
    private ActiveUserController activeUserController;
    private AttendanceController attendanceController;
    private ProgressDialog wrapperDialog;

    public AttendanceAdapter(Context context, List<Attendant> data, int layoutId, Reservation reservation) {
        super(context, data, layoutId);
        this.reservation = reservation;

        // create controllers
        activeUserController = new ActiveUserController(context);
        attendanceController = new AttendanceController();
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

        // set the name
        final Attendant item = data.get(position);
        holder.tvName.setText(item.getPlayer().getName());

        // load image
        Utils.loadImage(context, item.getPlayer().getImageLink(), R.drawable.default_image, holder.ivImage);

        // set confirm status suitable icon
        if (item.getType() == ReservationStatusType.CONFIRM.getValue()) {
            holder.ivConfirmStatus.setImageResource(R.drawable.green_true_icon);
        } else if (item.getType() == ReservationStatusType.DECLINE.getValue()) {
            holder.ivConfirmStatus.setImageResource(R.drawable.false_icon);
        } else {
            holder.ivConfirmStatus.setImageResource(R.drawable.exclamation_icon);
        }

        // check if current item is the current active user
        User user = activeUserController.getUser();
        if (attendanceController.isCurrentActiveUser(item, user.getId())) {
            // check confirm status and update ui
            if (item.getType() == ReservationStatusType.CONFIRM.getValue()) {
                // update confirm tv
                holder.tvConfirm.setText(R.string.confirmed);
                holder.tvConfirm.setEnabled(false);

                // enable refuse option
                enableRefuseOption(holder);
            } else if (item.getType() == ReservationStatusType.DECLINE.getValue()) {
                // update refuse tv
                holder.tvRefuse.setText(R.string.refused);
                holder.tvRefuse.setEnabled(false);

                // enable confirm option
                enableConfirmOption(holder);
            } else {
                // show two options (confirm and refuse)
                enableConfirmOption(holder);
                enableRefuseOption(holder);
            }
            // show actions layout and hide name
            holder.layoutActions.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.GONE);
        } else {
            // hide actions layout and show name
            holder.layoutActions.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.VISIBLE);
        }

        // create the global click listener
        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_confirm:
                        showConfirmDialog(position, true);
                        break;

                    case R.id.tv_refuse:
                        showConfirmDialog(position, false);
                        break;
                }
            }
        };

        // add listeners
        holder.tvConfirm.setOnClickListener(clickListener);
        holder.tvRefuse.setOnClickListener(clickListener);
    }

    private void enableConfirmOption(ViewHolder viewHolder) {
        viewHolder.tvConfirm.setText(R.string.confirm_your_attendance_u);
        viewHolder.tvConfirm.setEnabled(true);
    }

    private void enableRefuseOption(ViewHolder viewHolder) {
        viewHolder.tvRefuse.setText(R.string.refuse_u);
        viewHolder.tvRefuse.setEnabled(true);
    }

    private void showConfirmDialog(final int position, final boolean confirm) {
        int msgId = confirm ? R.string.confirm_your_attendance_q : R.string.refuse_u;
        DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirm(position, confirm);
            }
        }, null);
    }

    private void confirm(final int position, final boolean confirm) {
        // get the attendant
        final Attendant attendant = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgress();

        // create the connection listener
        ConnectionListener<String> listener = new ConnectionListener<String>() {
            @Override
            public void onSuccess(String response, int statusCode, String tag) {
                hideProgress();

                // check the status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg
                    Utils.showShortToast(context, confirm ? R.string.confirmed : R.string.refused);

                    // update this item
                    attendant.setType(confirm ? ReservationStatusType.CONFIRM.getValue()
                            : ReservationStatusType.DECLINE.getValue());
                    attendant.setTypeMessage(getString(confirm ? R.string.confirmed : R.string.refused));
                    notifyItemChanged(position);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, confirm ? R.string.failed_confirming : R.string.failed_refusing);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgress();
                Utils.showShortToast(context, confirm ? R.string.failed_confirming : R.string.failed_refusing);
            }
        };

        // prepare request params
        User user = activeUserController.getUser();
        Team reservationTeam = reservation.getReservationTeam();
        int confirmType = confirm ? ReservationConfirmType.CONFIRM.getValue()
                : ReservationConfirmType.DECLINE.getValue();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.confirmPresent(context, listener, user.getId(),
                user.getToken(), user.getName(), reservation.getId(), reservationTeam.getId(), confirmType);
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivImage;
        private ImageView ivConfirmStatus;
        private TextView tvName;
        private View layoutActions;
        private TextView tvConfirm;
        private TextView tvRefuse;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (ImageView) findViewById(R.id.iv_image);
            ivConfirmStatus = (ImageView) findViewById(R.id.iv_confirm_status);
            tvName = (TextView) findViewById(R.id.tv_name);
            layoutActions = findViewById(R.id.layout_actions);
            tvConfirm = (TextView) findViewById(R.id.tv_confirm);
            tvRefuse = (TextView) findViewById(R.id.tv_refuse);
        }
    }

    private void showProgress() {
        if (wrapperDialog != null) {
            wrapperDialog.showProgressView();
        } else {
            showProgressDialog();
        }
    }

    private void hideProgress() {
        if (wrapperDialog != null) {
            wrapperDialog.hideProgressView();
        } else {
            hideProgressDialog();
        }
    }

    public void setWrapperDialog(ProgressDialog wrapperDialog) {
        this.wrapperDialog = wrapperDialog;
    }
}
