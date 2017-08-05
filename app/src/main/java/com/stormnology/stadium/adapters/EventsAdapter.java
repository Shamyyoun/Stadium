package com.stormnology.stadium.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.PlayerInfoActivity;
import com.stormnology.stadium.activities.StadiumInfoActivity;
import com.stormnology.stadium.activities.TeamInfoActivity;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.Event;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ChallengesType;
import com.stormnology.stadium.models.enums.EventProfileType;
import com.stormnology.stadium.models.enums.EventType;
import com.stormnology.stadium.models.enums.ReservationConfirmType;
import com.stormnology.stadium.models.enums.ReservationStatusType;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class EventsAdapter extends ParentRecyclerAdapter<Event> {
    private static final String DISPLAY_DATE_FORMAT = "yyyy-M-d";
    private PrettyTime prettyTime;
    private ActiveUserController activeUserController;

    public EventsAdapter(Context context, List<Event> data) {
        super(context, data);
        prettyTime = new PrettyTime(new Locale("ar"));
        activeUserController = new ActiveUserController(context);
    }

    @Override
    public int getItemViewType(int position) {
        // get item type
        Event event = data.get(position);
        int type = event.getEventType();
        return type;
    }

    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create suitable view holder
        ViewHolder holder;
        if (viewType == EventType.STADIUM_RESERVED.getValue()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_reservation_event, parent, false);
            holder = new ReservationEventViewHolder(itemView);

        } else if (viewType == EventType.NEW_CHALLENGE.getValue()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_challenge_event, parent, false);
            holder = new ChallengeEventViewHolder(itemView);

        } else {
            // basic view holder
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_basic_event, parent, false);
            holder = new ViewHolder(itemView);
        }
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ParentRecyclerViewHolder viewHolder, final int position) {
        // get item and bind views
        ViewHolder holder = (ViewHolder) viewHolder;
        Event item = data.get(position);
        holder.bindViews(item);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        protected Event event;

        private View layoutContent;
        private TextView tvDate;
        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvMessage;

        public ViewHolder(final View itemView) {
            super(itemView);

            // init views
            layoutContent = findViewById(R.id.layout_content);
            tvDate = (TextView) findViewById(R.id.tv_date);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvMessage = (TextView) findViewById(R.id.tv_message);
        }

        public void bindViews(Event event) {
            this.event = event;

            // set basic data
            tvTitle.setText(event.getTitle());
            tvMessage.setText(event.getMessage());

            // set suitable date
            final Calendar calendar = DateUtils.convertToCalendar(event.getDate(), Event.DATE_FORMAT);
            String formattedDate;
            if (DateUtils.isToday(calendar)) {
                formattedDate = prettyTime.format(calendar);
            } else {
                formattedDate = DateUtils.formatDate(event.getDate(), Event.DATE_FORMAT, DISPLAY_DATE_FORMAT);
            }
            tvDate.setText(formattedDate);

            // load image
            Utils.loadImage(context, event.getImageLink(), R.drawable.default_image, ivImage);

            // add listeners
            ivImage.setOnClickListener(this);
            layoutContent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_image:
                    openInfoActivity(event.getPicType(), event.getPicId());
                    break;

                case R.id.layout_content:
                    openInfoActivity(event.getTitleType(), event.getTitleId());
                    break;

                default:
                    super.onClick(v);
            }
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
    }

    class ReservationEventViewHolder extends ViewHolder {
        private View layoutButtons;
        private Button btnConfirm;
        private Button btnDecline;
        private TextView tvConfirmStatus;

        public ReservationEventViewHolder(final View itemView) {
            super(itemView);

            // init views
            layoutButtons = findViewById(R.id.layout_buttons);
            btnConfirm = (Button) findViewById(R.id.btn_confirm);
            btnDecline = (Button) findViewById(R.id.btn_decline);
            tvConfirmStatus = (TextView) findViewById(R.id.tv_confirm_status);
        }

        @Override
        public void bindViews(Event event) {
            super.bindViews(event);

            // check the confirm status id
            if (event.getConfirmStatusId() == ReservationStatusType.NO_ACTION.getValue()) {
                tvConfirmStatus.setText(R.string.you_are_out_by_the_captain);
            } else if (event.getConfirmStatusId() == ReservationStatusType.CONFIRM.getValue()) {
                String confirmStatus = !Utils.isNullOrEmpty(event.getConfirmStatus()) ?
                        event.getConfirmStatus() : getString(R.string.confirmed);
                tvConfirmStatus.setText(confirmStatus);
            } else if (event.getConfirmStatusId() == ReservationStatusType.DECLINE.getValue()) {
                String confirmStatus = !Utils.isNullOrEmpty(event.getConfirmStatus()) ?
                        event.getConfirmStatus() : getString(R.string.decline);
                tvConfirmStatus.setText(confirmStatus);
            } else {
                // otherwise, show the buttons layout
                layoutButtons.setVisibility(View.VISIBLE);
                tvConfirmStatus.setVisibility(View.GONE);
            }

            // add listeners
            btnConfirm.setOnClickListener(this);
            btnDecline.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    showConfirmDialog(true);
                    break;

                case R.id.btn_decline:
                    showConfirmDialog(false);
                    break;

                default:
                    super.onClick(v);
            }
        }

        private void showConfirmDialog(final boolean confirm) {
            int msgId = confirm ? R.string.confirm_this_reservation_q : R.string.decline_this_reservation_q;
            DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirm(confirm);
                }
            }, null);
        }

        private void confirm(final boolean confirm) {
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

                        // then notify the adapter
                        data.set(getPosition(), event);
                        notifyItemChanged(getPosition());
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
            User user = activeUserController.getUser();
            final int confirmType = confirm ? ReservationConfirmType.CONFIRM.getValue()
                    : ReservationConfirmType.DECLINE.getValue();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.confirmPresent(context, listener, user.getId(),
                    user.getToken(), user.getName(), event.getResId(), event.getTitleId(), confirmType);
            cancelWhenDestroyed(connectionHandler);
        }
    }

    class ChallengeEventViewHolder extends ViewHolder {
        private View layoutButtons;
        private Button btnAccept;

        public ChallengeEventViewHolder(final View itemView) {
            super(itemView);

            // init views
            layoutButtons = findViewById(R.id.layout_buttons);
            btnAccept = (Button) findViewById(R.id.btn_accept);
        }

        @Override
        public void bindViews(Event event) {
            super.bindViews(event);

            // get current user
            User user = activeUserController.getUser();

            // prepare flags
            boolean isNewChallenge = event.getChallengeType() == ChallengesType.NEW_CHALLENGES.getValue();
            boolean isGuestAdmin = user.getId() == event.getGuestCaptain() || user.getId() == event.getGuestAssistant();
            boolean canAccept = isNewChallenge && isGuestAdmin;

            // show / hide buttons layout
            layoutButtons.setVisibility(canAccept ? View.VISIBLE : View.GONE);

            // add listeners
            btnAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_accept:
                    showAcceptConfirmDialog();
                    break;

                default:
                    super.onClick(v);
            }
        }

        private void showAcceptConfirmDialog() {
            DialogUtils.showConfirmDialog(context, R.string.accept_challenge_q, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    accept();
                }
            }, null);
        }

        private void accept() {
            // check internet connection
            if (!Utils.hasConnection(context)) {
                Utils.showShortToast(context, R.string.no_internet_connection);
                return;
            }

            showProgressDialog();

            // create the connection listener
            ConnectionListener<Challenge> listener = new ConnectionListener<Challenge>() {
                @Override
                public void onSuccess(Challenge response, int statusCode, String tag) {
                    hideProgressDialog();

                    // check the status code
                    if (statusCode == Const.SER_CODE_200) {
                        // show success msg
                        Utils.showShortToast(context, R.string.accepted_successfully);

                        // and update this item
                        event.setChallengeType(ChallengesType.ACCEPTED_CHALLENGES.getValue());
                        data.set(getPosition(), event);
                        notifyItemChanged(getPosition());
                    } else {
                        // show error msg
                        String errorMsg = AppUtils.getResponseMsg(context, response, R.string.error_accepting);
                        Utils.showShortToast(context, errorMsg);
                    }
                }

                @Override
                public void onFail(Exception ex, int statusCode, String tag) {
                    hideProgressDialog();
                    Utils.showShortToast(context, R.string.error_accepting);
                }
            };

            // prepare objects
            User user = activeUserController.getUser();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.acceptChallenge(context, listener,
                    user.getId(), user.getToken(), event.getChallengeId());
            cancelWhenDestroyed(connectionHandler);
        }
    }
}
