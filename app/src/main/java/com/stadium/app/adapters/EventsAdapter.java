package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.UserController;
import com.stadium.app.models.entities.Event;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.enums.EventType;
import com.stadium.app.models.responses.ServerResponse;
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
    private UserController userController;

    public EventsAdapter(Context context, List<Event> data, int layoutId) {
        super(context, data, layoutId);
        prettyTime = new PrettyTime(new Locale("ar"));
        userController = new UserController(context);
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
        Calendar calendar = DateUtils.convertToCalendar(item.getDate(), Event.DATE_FORMAT);
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
            holder.layoutButtons.setVisibility(View.VISIBLE);
            holder.tvConfirmStatus.setVisibility(View.GONE);
        } else if (item.getEventType() == EventType.RESERVATION_RESPONDED.getValue()) {
            holder.layoutBottomWrapper.setVisibility(View.VISIBLE);
            holder.layoutButtons.setVisibility(View.GONE);
            holder.tvConfirmStatus.setVisibility(View.VISIBLE);

            // set status
            String confirmStatusStr = getString(item.getConfirmStatus() == Const.EVENT_STATUS_CONFIRM ?
                    R.string.confirmed : R.string.declined);
            holder.tvConfirmStatus.setText(confirmStatusStr);
        } else {
            holder.layoutBottomWrapper.setVisibility(View.GONE);
        }

        // add listeners
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(position, true);
            }
        });
        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(position, false);
            }
        });
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

        // prepare request params
        User user = userController.getUser();
        final int confirmType = confirm ? Const.EVENT_STATUS_CONFIRM : Const.EVENT_STATUS_DECLINE;

        // create the connection listener
        ConnectionListener<String> listener = new ConnectionListener<String>() {
            @Override
            public void onSuccess(String response, int statusCode, String tag) {
                hideProgressDialog();

                // check the status code
                if (statusCode == Const.SER_CODE_200) {
                    String msg;
                    if (!Utils.isNullOrEmpty(response)) {
                        msg = response;
                    } else {
                        msg = getString(confirm ? R.string.accepted_successfully : R.string.declined_successfully);
                    }

                    // show msg
                    Utils.showShortToast(context, msg);

                    // update this item
                    event.setEventType(EventType.RESERVATION_RESPONDED.getValue());
                    event.setConfirmStatus(confirm ? Const.EVENT_STATUS_CONFIRM : Const.EVENT_STATUS_DECLINE);
                    notifyItemChanged(position);
                } else {
                    // parse the response
                    ServerResponse serverResponse = null;
                    try {
                        serverResponse = new Gson().fromJson(response, ServerResponse.class);
                    } catch (Exception e) {
                    }

                    String msg;
                    if (serverResponse != null && !Utils.isNullOrEmpty(serverResponse.getErrorMessage())) {
                        msg = serverResponse.getErrorMessage();
                    } else {
                        msg = getString(confirm ? R.string.error_accepting : R.string.error_declining);
                    }

                    // show msg
                    Utils.showShortToast(context, msg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, confirm ? R.string.error_accepting : R.string.error_declining);
            }
        };

        // send request
        ConnectionHandler connectionHandler = ApiRequests.confirmPresent(context, listener, user.getId(),
                user.getToken(), event.getResId(), confirmType);
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
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
