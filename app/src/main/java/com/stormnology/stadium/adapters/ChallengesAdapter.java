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
import com.stormnology.stadium.activities.TeamInfoActivity;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.ChallengeController;
import com.stormnology.stadium.controllers.TeamController;
import com.stormnology.stadium.dialogs.AddChallengeResultDialog;
import com.stormnology.stadium.dialogs.ChooseReservationDialog;
import com.stormnology.stadium.interfaces.OnChallengeUpdatedListener;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ChallengesType;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ChallengesAdapter extends ParentRecyclerAdapter<Challenge> {
    private ChallengesType challengesType;

    private ActiveUserController activeUserController;
    private ChallengeController challengeController;
    private TeamController teamController;
    private PrettyTime prettyTime;

    public ChallengesAdapter(Context context, List<Challenge> data) {
        super(context, data);

        // obtain main objects
        activeUserController = new ActiveUserController(context);
        challengeController = new ChallengeController();
        teamController = new TeamController();
        prettyTime = new PrettyTime(new Locale("ar"));
    }

    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create suitable view holder
        ViewHolder holder;
        if (viewType == ChallengesType.NEW_CHALLENGES.getValue()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_challenge, parent, false);
            holder = new NewChallengeViewHolder(itemView);

        } else if (viewType == ChallengesType.ACCEPTED_CHALLENGES.getValue()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_accepted_challenge, parent, false);
            holder = new AcceptedChallengeViewHolder(itemView);

        } else if (viewType == ChallengesType.HISTORICAL_CHALLENGES.getValue()) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_historical_challenge, parent, false);
            holder = new HistoricalChallengeViewHolder(itemView);

        } else {
            // return basic view holder
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_main_challenge, parent, false);
            holder = new ViewHolder(itemView);
        }
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        // get item type
        Challenge challenge = data.get(position);
        int type = challenge.getType() != null ? challenge.getType().getId() : 0;

        // check challengesType
        if (challengesType == ChallengesType.MY_CHALLENGES) {
            // this is my challenge, so return it
            return type;
        } else {
            // return challengesType value
            return challengesType.getValue();
        }
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

        private ChooseReservationDialog reservationsDialog;
        private AddChallengeResultDialog addResultDialog;

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

            // check if the challenge for all to enable / disable the guest team image
            boolean challengeForAll = challengeController.isChallengeForAll(challenge);
            ivGuestTeamImage.setEnabled(!challengeForAll);

            // add listeners
            ivHostTeamImage.setOnClickListener(this);
            ivGuestTeamImage.setOnClickListener(this);
        }

        protected void updateScoresUI() {
            // check the challenge
            if (challenge == null) {
                return;
            }

            // set host team score
            if (challengeController.hasHostScore(challenge)) {
                tvHostTeamScore.setText("" + challenge.getHostGoals());
            } else {
                tvHostTeamScore.setText("--");
            }

            // set guest team score
            if (challengeController.hasGuestScore(challenge)) {
                tvGuestTeamScore.setText("" + challenge.getGuestGoals());
            } else {
                tvGuestTeamScore.setText("--");
            }
        }

        protected void removeScores() {
            tvHostTeamScore.setText("--");
            tvGuestTeamScore.setText("--");
        }

        @Override
        public void onClick(View v) {
            // check view id
            if (v.getId() == R.id.iv_host_team_image) {
                openTeamInfoActivity(challenge.getHostTeam().getId());
            } else if (v.getId() == R.id.iv_guest_team_image) {
                openTeamInfoActivity(challenge.getGuestTeam().getId());
            }
        }

        private void openTeamInfoActivity(int teamId) {
            Intent intent = new Intent(context, TeamInfoActivity.class);
            intent.putExtra(Const.KEY_ID, teamId);
            context.startActivity(intent);
        }

        protected void showAcceptConfirmDialog() {
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

                        // and remove this item
                        removeItem(getPosition());
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
            Team hostTeam = challenge.getHostTeam();
            Team guestTeam = challenge.getGuestTeam();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.acceptChallenge(context, listener,
                    user.getId(), user.getToken(), challenge.getId(), hostTeam.getId(),
                    hostTeam.getName(), guestTeam.getId(), guestTeam.getName());
            cancelWhenDestroyed(connectionHandler);
        }

        protected void showWithdrawConfirmDialog() {
            DialogUtils.showConfirmDialog(context, R.string.withdraw_from_challenge_q, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    withdraw();
                }
            }, null);
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
                            removeItem(getPosition());
                        } else {
                            // user is an admin in the guest team
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

            // prepare objects
            User user = activeUserController.getUser();
            Team hostTeam = challenge.getHostTeam();
            Team guestTeam = challenge.getGuestTeam();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.leaveChallenge(context, listener,
                    user.getId(), user.getToken(), challenge.getId(), hostTeam.getId(),
                    hostTeam.getName(), guestTeam.getId(), guestTeam.getName());
            cancelWhenDestroyed(connectionHandler);
        }

        protected void chooseReservation() {
            // create & customize the dialog if required
            if (reservationsDialog == null) {
                reservationsDialog = new ChooseReservationDialog(context, challenge.getHostTeam().getId());
                reservationsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                    @Override
                    public void onCheckableSelected(Checkable item) {
                        addReservation((Reservation) item);
                    }
                });
            }

            // show the dialog
            reservationsDialog.show();
        }

        private void addReservation(Reservation reservation) {
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
                        Utils.showShortToast(context, R.string.reservation_added_successfully);

                        // update this item with the new object
                        data.set(getPosition(), response);
                        notifyItemChanged(getPosition());
                    } else {
                        // show error msg
                        String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_adding_reservation);
                        Utils.showShortToast(context, errorMsg);
                    }
                }

                @Override
                public void onFail(Exception ex, int statusCode, String tag) {
                    hideProgressDialog();
                    Utils.showShortToast(context, R.string.failed_adding_reservation);
                }
            };

            // prepare objects
            User user = activeUserController.getUser();
            Team hostTeam = challenge.getHostTeam();
            Team guestTeam = challenge.getGuestTeam();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.addResToChallenge(context, listener,
                    user.getId(), user.getToken(), challenge.getId(), reservation.getId(), hostTeam.getId(),
                    hostTeam.getName(), guestTeam.getId(), guestTeam.getName());
            cancelWhenDestroyed(connectionHandler);
        }

        protected void showObjectConfirmDialog() {
            DialogUtils.showConfirmDialog(context, R.string.object_to_result_q, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    objectToResult();
                }
            }, null);
        }

        private void objectToResult() {
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
                        Utils.showShortToast(context, R.string.objected_successfully);

                        // update this item with the new object
                        data.set(getPosition(), response);
                        notifyItemChanged(getPosition());
                    } else {
                        // show error msg
                        String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_objection);
                        Utils.showShortToast(context, errorMsg);
                    }
                }

                @Override
                public void onFail(Exception ex, int statusCode, String tag) {
                    hideProgressDialog();
                    Utils.showShortToast(context, R.string.failed_objection);
                }
            };

            // prepare objects
            User user = activeUserController.getUser();
            Team hostTeam = challenge.getHostTeam();
            Team guestTeam = challenge.getGuestTeam();

            // send request
            ConnectionHandler connectionHandler = ApiRequests.resultObjection(context, listener,
                    user.getId(), user.getToken(), challenge.getId(), hostTeam.getId(),
                    hostTeam.getName(), guestTeam.getId(), guestTeam.getName());
            cancelWhenDestroyed(connectionHandler);
        }

        protected void showAddResultDialog() {
            // create the dialog if required
            if (addResultDialog == null) {
                addResultDialog = new AddChallengeResultDialog(context, challenge);
                addResultDialog.setOnChallengeUpdatedListener(new OnChallengeUpdatedListener() {
                    @Override
                    public void onChallengeUpdated(Challenge challenge) {
                        // update in the adapter
                        data.set(getPosition(), challenge);
                        notifyItemChanged(getPosition());
                    }
                });
            }

            // show the dialog
            addResultDialog.show();
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

            // remove scores from the UI
            removeScores();

            // get the user
            User user = activeUserController.getUser();

            // prepare user roles
            boolean hostTeamAdmin = teamController.isCaptain(challenge.getHostTeam(), user.getId())
                    || teamController.isAssistant(challenge.getHostTeam(), user.getId());
            boolean guestTeamAdmin = teamController.isCaptain(challenge.getGuestTeam(), user.getId())
                    || teamController.isAssistant(challenge.getGuestTeam(), user.getId());

            // prepare flags
            boolean challengeForAll = challengeController.isChallengeForAll(challenge);
            boolean canAccept = !hostTeamAdmin && ((challengeForAll && challenge.isCaptainRole()) || guestTeamAdmin);

            // show / hide buttons layout
            layoutButtons.setVisibility(canAccept ? View.VISIBLE : View.GONE);

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

            // remove scores from the UI
            removeScores();

            // get the user
            User user = activeUserController.getUser();

            // prepare user roles
            boolean hostTeamAdmin = teamController.isCaptain(challenge.getHostTeam(), user.getId())
                    || teamController.isAssistant(challenge.getHostTeam(), user.getId());
            boolean guestTeamAdmin = teamController.isCaptain(challenge.getGuestTeam(), user.getId())
                    || teamController.isAssistant(challenge.getGuestTeam(), user.getId());

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
                chooseReservation();
            } else if (v.getId() == R.id.btn_withdraw) {
                showWithdrawConfirmDialog();
            } else {
                super.onClick(v);
            }
        }
    }

    class HistoricalChallengeViewHolder extends ViewHolder {
        private View layoutButtons;
        private Button btnAction;
        private boolean canAddResult;
        private boolean canObjectScores;

        public HistoricalChallengeViewHolder(final View itemView) {
            super(itemView);

            layoutButtons = findViewById(R.id.layout_buttons);
            btnAction = (Button) findViewById(R.id.btn_action);
        }

        @Override
        public void bindViews(Challenge challenge) {
            super.bindViews(challenge);

            // update scores UI
            updateScoresUI();

            // get the user
            User user = activeUserController.getUser();

            // prepare user roles
            boolean hostTeamAdmin = teamController.isCaptain(challenge.getHostTeam(), user.getId())
                    || teamController.isAssistant(challenge.getHostTeam(), user.getId());
            boolean guestTeamAdmin = teamController.isCaptain(challenge.getGuestTeam(), user.getId())
                    || teamController.isAssistant(challenge.getGuestTeam(), user.getId());

            // prepare flags
            canAddResult = hostTeamAdmin && !challengeController.hasHostScore(challenge);
            canObjectScores = guestTeamAdmin && challengeController.hasGuestScore(challenge);
            boolean canTakeAction = canAddResult || canObjectScores;

            // customize the action button
            if (canAddResult) {
                btnAction.setText(R.string.add_result);
                btnAction.setTextColor(getResColor(R.color.dark_gray));
                btnAction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.confirm_icon, 0, 0, 0);
            } else if (canObjectScores) {
                btnAction.setText(R.string.object_to_result);
                btnAction.setTextColor(getResColor(R.color.red));
                btnAction.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_warning_icon, 0, 0, 0);
            }

            // show / hide buttons layout
            layoutButtons.setVisibility(canTakeAction ? View.VISIBLE : View.GONE);

            // add listeners
            btnAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // check view id
            if (v.getId() == R.id.btn_action) {
                onAction();
            } else {
                super.onClick(v);
            }
        }

        private void onAction() {
            // check the action
            if (canAddResult) {
                showAddResultDialog();
            } else if (canObjectScores) {
                showObjectConfirmDialog();
            }
        }
    }
}
