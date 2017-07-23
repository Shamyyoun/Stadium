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
import com.stormnology.stadium.controllers.ChallengeController;
import com.stormnology.stadium.controllers.TeamController;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ChallengesType;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

import static com.stormnology.stadium.R.string.position;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ChallengesAdapter extends ParentRecyclerAdapter<Challenge> {
    private ChallengesType challengesType;

    private ActiveUserController activeUserController;
    private ChallengeController challengeController;
    private TeamController teamController;
    private PrettyTime prettyTime;

    public ChallengesAdapter(Context context, List<Challenge> data, int layoutId) {
        super(context, data, layoutId);

        // obtain main objects
        activeUserController = new ActiveUserController(context);
        challengeController = new ChallengeController();
        teamController = new TeamController();
        prettyTime = new PrettyTime(new Locale("ar"));
    }

    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // create suitable view holder
        ViewHolder holder;
        if (challengesType == ChallengesType.NEW_CHALLENGES) {
            holder = new NewChallengeViewHolder(itemView);
        } else if (challengesType == ChallengesType.ACCEPTED_CHALLENGES) {
            holder = new AcceptedChallengeViewHolder(itemView);
        } else {
            holder = new NewChallengeViewHolder(itemView);
        }
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ParentRecyclerViewHolder viewHolder, final int position) {
        // get item and bind views
        ViewHolder holder = (ViewHolder) viewHolder;
        Challenge item = data.get(position);
        holder.bindViews(item);
    }

    public void setChallengesType(ChallengesType challengesType) {
        this.challengesType = challengesType;
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        protected Challenge challenge;

        private TextView tvCreationDate;
        private TextView tvHostTeamName;
        private ImageView ivHostTeamImage;
        private TextView tvGuestTeamName;
        private ImageView ivGuestTeamImage;
        private TextView tvComment;
        private TextView tvHostTeamScore;
        private TextView tvGuestTeamScore;
        private View viewTopDivider;
        private TextView tvPlace;
        private TextView tvDateTime;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvCreationDate = (TextView) findViewById(R.id.tv_creation_date);
            tvHostTeamName = (TextView) findViewById(R.id.tv_host_team_name);
            ivHostTeamImage = (ImageView) findViewById(R.id.iv_host_team_image);
            tvGuestTeamName = (TextView) findViewById(R.id.tv_guest_team_name);
            ivGuestTeamImage = (ImageView) findViewById(R.id.iv_guest_team_image);
            tvComment = (TextView) findViewById(R.id.tv_comment);
            tvHostTeamScore = (TextView) findViewById(R.id.tv_host_team_score);
            tvGuestTeamScore = (TextView) findViewById(R.id.tv_guest_team_score);
            viewTopDivider = findViewById(R.id.view_top_divider);
            tvPlace = (TextView) findViewById(R.id.tv_place);
            tvDateTime = (TextView) findViewById(R.id.tv_date_time);
        }

        public void bindViews(Challenge challenge) {
            // set the challenge
            this.challenge = challenge;

            // set date
            String creationDate = challengeController.getCreationDate(challenge, prettyTime);
            if (creationDate != null) {
                tvCreationDate.setText(creationDate);
                tvCreationDate.setVisibility(View.VISIBLE);
            } else {
                tvCreationDate.setVisibility(View.GONE);
            }

            // set host team info
            Team hostTeam = challenge.getHostTeam();
            tvHostTeamName.setText(challengeController.getHostTeamName(challenge));
            Utils.loadImage(context, hostTeam.getImageLink(), R.drawable.default_image, ivHostTeamImage);

            // set guest team info
            Team guestTeam = challenge.getGuestTeam();
            tvGuestTeamName.setText(challengeController.getGuestTeamName(context, challenge));
            Utils.loadImage(context, guestTeam.getImageLink(), R.drawable.default_image, ivGuestTeamImage);

            // set comment
            if (!Utils.isNullOrEmpty(challenge.getHostComment())) {
                tvComment.setText(challenge.getHostComment());
                tvComment.setVisibility(View.VISIBLE);
            } else {
                tvComment.setVisibility(View.GONE);
            }

            // set place info
            String placeInfo = challengeController.getPlaceInfo(context, challenge);
            if (!Utils.isNullOrEmpty(placeInfo)) {
                tvPlace.setText(placeInfo);
                tvPlace.setVisibility(View.VISIBLE);
            } else {
                tvPlace.setVisibility(View.GONE);
            }

            // set date info
            String dateInfo = challengeController.getDateInfo(challenge);
            if (!Utils.isNullOrEmpty(dateInfo)) {
                tvDateTime.setText(dateInfo);
                tvDateTime.setVisibility(View.VISIBLE);
            } else {
                tvDateTime.setVisibility(View.GONE);
            }

            // check to show / hide the top divider
            if (Utils.isNullOrEmpty(placeInfo) && Utils.isNullOrEmpty(dateInfo)) {
                viewTopDivider.setVisibility(View.GONE);
            } else {
                viewTopDivider.setVisibility(View.VISIBLE);
            }
        }

        protected void showAcceptConfirmDialog() {
            DialogUtils.showConfirmDialog(context, R.string.accept_challenge_q, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    accept();
                }
            }, null);
        }

        protected void showWithdrawConfirmDialog() {
            DialogUtils.showConfirmDialog(context, R.string.withdraw_from_challenge_q, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    withdraw();
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

                        // and remove this item
                        removeItem(position);
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

            // get active user
            User user = activeUserController.getUser();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.acceptChallenge(context, listener,
                    user.getId(), user.getToken(), challenge.getId(), challenge.getGuestTeam().getId());
            cancelWhenDestroyed(connectionHandler);
        }

        private void withdraw() {
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
                        Utils.showShortToast(context, R.string.withdrawn_successfully);

                        // check user role in challenge teams
                        User user = activeUserController.getUser();
                        if (teamController.isCaptain(challenge.getHostTeam(), user.getId())
                                || teamController.isAssistant(challenge.getHostTeam(), user.getId())) {
                            // user is an admin the host team
                            // so remove this item from the adapter
                            removeItem(position);
                        } else {
                            // user is an admin the guest team
                            // so, just update this item with the new object
                            data.set(getPosition(), response);
                            notifyItemChanged(getPosition());
                        }
                    } else {
                        // show error msg
                        String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_withdrawing);
                        Utils.showShortToast(context, errorMsg);
                    }
                }

                @Override
                public void onFail(Exception ex, int statusCode, String tag) {
                    hideProgressDialog();
                    Utils.showShortToast(context, R.string.failed_withdrawing);
                }
            };

            // get active user
            User user = activeUserController.getUser();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.leaveChallenge(context, listener,
                    user.getId(), user.getToken(), challenge.getId());
            cancelWhenDestroyed(connectionHandler);
        }
    }

    class NewChallengeViewHolder extends ViewHolder {
        private View layoutButtons;
        private Button btnAccept;

        public NewChallengeViewHolder(final View itemView) {
            super(itemView);

            layoutButtons = findViewById(R.id.layout_buttons);
            btnAccept = (Button) findViewById(R.id.btn_accept);
        }

        @Override
        public void bindViews(Challenge challenge) {
            super.bindViews(challenge);

            // get the user
            User user = activeUserController.getUser();

            // check his role in the host team
            if (teamController.isCaptain(challenge.getHostTeam(), user.getId())
                    || teamController.isAssistant(challenge.getHostTeam(), user.getId())) {
                // hide buttons layout
                layoutButtons.setVisibility(View.GONE);
            } else if (challengeController.isChallengeForAll(challenge)) { // check if the challenge is for all
                // check his captainRole to show / hide the buttons layout
                boolean captainRole = challenge.isCaptainRole();

                layoutButtons.setVisibility(captainRole ? View.VISIBLE : View.GONE);
            } else {
                // check his role in the guest team to show / hide buttons layout
                boolean admin = teamController.isCaptain(challenge.getGuestTeam(), user.getId())
                        || teamController.isAssistant(challenge.getGuestTeam(), user.getId());

                layoutButtons.setVisibility(admin ? View.VISIBLE : View.GONE);
            }

            // add listeners
            btnAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // check view id
            if (v.getId() == R.id.btn_accept) {
                showAcceptConfirmDialog();
            } else {
                super.onClick(v);
            }
        }
    }

    class AcceptedChallengeViewHolder extends ViewHolder {
        private View layoutButtons;
        private Button btnAddRes;
        private Button btnWithdraw;
        private View viewDivider;

        public AcceptedChallengeViewHolder(final View itemView) {
            super(itemView);

            layoutButtons = findViewById(R.id.layout_buttons);
            btnAddRes = (Button) findViewById(R.id.btn_add_res);
            btnWithdraw = (Button) findViewById(R.id.btn_withdraw);
            viewDivider = findViewById(R.id.view_divider);
        }

        @Override
        public void bindViews(Challenge challenge) {
            super.bindViews(challenge);

            // get the user
            User user = activeUserController.getUser();

            // prepare user roles
            boolean hostTeamAdmin = teamController.isCaptain(challenge.getHostTeam(), user.getId())
                    || teamController.isAssistant(challenge.getHostTeam(), user.getId());
            boolean guestTeamAdmin = teamController.isCaptain(challenge.getHostTeam(), user.getId())
                    || teamController.isAssistant(challenge.getHostTeam(), user.getId());

            // prepare flags
            boolean canWithdraw = hostTeamAdmin || guestTeamAdmin;
            boolean canAddRes = hostTeamAdmin && challenge.getReservation() == null;

            // show / hide withdraw btn
            btnWithdraw.setVisibility(canWithdraw ? View.VISIBLE : View.GONE);

            // show / hide add res btn
            btnAddRes.setVisibility(canAddRes ? View.VISIBLE : View.GONE);

            // customize the whole view
            if (!canWithdraw && !canAddRes) {
                // he can't take any action
                // hide buttons layout
                layoutButtons.setVisibility(View.GONE);
            } else {
                if (canWithdraw && canAddRes) {
                    // he can take the two actions
                    // show divider view
                    viewDivider.setVisibility(View.VISIBLE);
                } else {
                    // he can take just one action
                    // hide the divider view
                    viewDivider.setVisibility(View.GONE);
                }
            }

            // add listeners
            btnAddRes.setOnClickListener(this);
            btnWithdraw.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // check view id
            if (v.getId() == R.id.btn_add_res) {
                // TODO
            } else if (v.getId() == R.id.btn_withdraw) {
                showWithdrawConfirmDialog();
            } else {
                super.onClick(v);
            }
        }
    }
}
