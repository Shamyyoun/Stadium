package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.PlayerInfoActivity;
import com.stadium.app.activities.StadiumInfoActivity;
import com.stadium.app.activities.TeamInfoActivity;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.models.entities.Event;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.enums.EventProfileType;
import com.stadium.app.models.enums.EventType;
import com.stadium.app.models.enums.ReservationConfirmType;
import com.stadium.app.models.enums.ReservationStatusType;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class EventsAdapter extends ParentRecyclerAdapter<Event> {
    private PrettyTime prettyTime;
    private ActiveUserController userController;

    public EventsAdapter(Context context, List<Event> data, int layoutId) {
        super(context, data, layoutId);
        prettyTime = new PrettyTime(new Locale("ar"));
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

        // get object
        final Event item = data.get(position);

        // set basic data
        holder.tvTitle.setText(item.getTitle());
        holder.tvMessage.setText(item.getMessage());

        // set suitable date
        final Calendar calendar = DateUtils.convertToCalendar(item.getDate(), Event.DATE_FORMAT);
        String formattedDate;
        if (DateUtils.isToday(calendar)) {
            formattedDate = prettyTime.format(calendar);
        } else {
            formattedDate = DateUtils.formatDate(item.getDate(), Event.DATE_FORMAT, "yyyy-M-d");
        }
        holder.tvDate.setText(formattedDate);

        // load image
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);

        // check event type to set the bottom values
        if (item.getEventType() == EventType.STADIUM_RESERVED.getValue()) {
            holder.layoutBottomWrapper.setVisibility(View.VISIBLE);
            holder.layoutButtons.setVisibility(View.GONE);
            holder.tvConfirmStatus.setVisibility(View.VISIBLE);

            // check the confirm status id
            if (item.getConfirmStatusId() == ReservationStatusType.NO_ACTION.getValue()) {
                holder.tvConfirmStatus.setText(R.string.you_are_out_by_the_captain);
            } else if (item.getConfirmStatusId() == ReservationStatusType.CONFIRM.getValue()) {
                String confirmStatus = !Utils.isNullOrEmpty(item.getConfirmStatus()) ?
                        item.getConfirmStatus() : getString(R.string.confirmed);
                holder.tvConfirmStatus.setText(confirmStatus);
            } else if (item.getConfirmStatusId() == ReservationStatusType.DECLINE.getValue()) {
                String confirmStatus = !Utils.isNullOrEmpty(item.getConfirmStatus()) ?
                        item.getConfirmStatus() : getString(R.string.decline);
                holder.tvConfirmStatus.setText(confirmStatus);
            } else {
                holder.layoutButtons.setVisibility(View.VISIBLE);
                holder.tvConfirmStatus.setVisibility(View.GONE);
            }
        } else {
            holder.layoutBottomWrapper.setVisibility(View.GONE);
        }

        // create the global click listener
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_image:
                        openInfoActivity(item.getPicType(), item.getPicId());
                        break;

                    case R.id.layout_content:
                        openInfoActivity(item.getTitleType(), item.getTitleId());
                        break;

                    case R.id.btn_confirm:
                        showConfirmDialog(position, true);
                        break;

                    case R.id.btn_decline:
                        showConfirmDialog(position, false);
                        break;
                }
            }
        };

        // add listeners
        holder.ivImage.setOnClickListener(clickListener);
        holder.layoutContent.setOnClickListener(clickListener);
        holder.btnConfirm.setOnClickListener(clickListener);
        holder.btnDecline.setOnClickListener(clickListener);
    }

    private void openInfoActivity(int type, int id) {
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_ID, id);

        // check type to open suitable activity
        if (type == EventProfileType.PLAYER.getValue()) {
            intent.setClass(context, PlayerInfoActivity.class);
        } else if (type == EventProfileType.TEAM.getValue()) {
            intent.setClass(context, TeamInfoActivity.class);
        } else if (type == EventProfileType.STADIUM.getValue()) {
            intent.setClass(context, StadiumInfoActivity.class);
        }

        // open the activity if possible
        if (intent.getClass() != null) {
            context.startActivity(intent);
        }
    }

    private void showConfirmDialog(final int position, final boolean confirm) {
        int msgId = confirm ? R.string.confirm_this_reservation_q : R.string.decline_this_reservation_q;
        DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirm(position, confirm);
            }
        }, null);
    }

    private void confirm(final int position, final boolean confirm) {
        // get the event
        final Event event = data.get(position);

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
                    // show success msg
                    Utils.showShortToast(context, confirm ? R.string.confirmed : R.string.decline);

                    // update this item
                    event.setConfirmStatusId(confirm ? ReservationStatusType.CONFIRM.getValue()
                            : ReservationStatusType.DECLINE.getValue());
                    event.setConfirmStatus(getString(confirm ? R.string.confirmed : R.string.decline));
                    notifyItemChanged(position);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, confirm ? R.string.error_accepting : R.string.error_declining);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, confirm ? R.string.error_accepting : R.string.error_declining);
            }
        };

        // prepare request params
        User user = userController.getUser();
        final int confirmType = confirm ? ReservationConfirmType.CONFIRM.getValue()
                : ReservationConfirmType.DECLINE.getValue();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.confirmPresent(context, listener, user.getId(),
                user.getToken(), event.getResId(), confirmType);
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private TextView tvDate;
        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvMessage;
        private View layoutBottomWrapper;
        private View layoutButtons;
        private Button btnConfirm;
        private Button btnDecline;
        private TextView tvConfirmStatus;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            tvDate = (TextView) findViewById(R.id.tv_date);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvMessage = (TextView) findViewById(R.id.tv_message);
            layoutBottomWrapper = findViewById(R.id.layout_bottom_wrapper);
            layoutButtons = findViewById(R.id.layout_buttons);
            btnConfirm = (Button) findViewById(R.id.btn_confirm);
            btnDecline = (Button) findViewById(R.id.btn_decline);
            tvConfirmStatus = (TextView) findViewById(R.id.tv_confirm_status);
        }
    }
}
