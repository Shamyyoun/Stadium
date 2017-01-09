package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.TeamController;
import com.stadium.app.dialogs.ChoosePlayerDialog;
import com.stadium.app.interfaces.OnUserSelectedListener;
import com.stadium.app.models.entities.PlayerRole;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class TeamsAdapter extends ParentRecyclerAdapter<Team> {
    private ActiveUserController userController;
    private TeamController teamController;
    private int playerId; // the player id to check his role in the every team and display it if required
    private int checkedItemPosition = -1; // used to hold last checked item position to uncheck it after checking new item

    public TeamsAdapter(Context context, List<Team> data, int layoutId) {
        super(context, data, layoutId);
        userController = new ActiveUserController(context);
        teamController = new TeamController();
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

        // set data
        Team item = data.get(position);
        holder.tvTitle.setText(item.getName());
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivLogo);

        // check to set the captain name
        if (holder.tvCaptainName != null) {
            // check the captain name
            if (item.getCaptain() != null && !Utils.isNullOrEmpty(item.getCaptain().getName())) {
                // set the captain name
                String captainName = item.getCaptain().getName();
                String captainNameStr = getString(R.string.captain) + " . " + captainName;
                holder.tvCaptainName.setText(captainNameStr);
                holder.tvCaptainName.setVisibility(View.VISIBLE);
            } else {
                holder.tvCaptainName.setVisibility(View.GONE);
            }
        }

        // check to set the role
        if (holder.tvRole != null) {
            // set the role
            PlayerRole role = teamController.getPlayerRole(item, playerId);
            if (role != null) {
                holder.tvRole.setText(role.getChar());
                holder.tvRole.setBackgroundResource(role.getBackgroundResId());
                holder.tvRole.setVisibility(View.VISIBLE);
            } else {
                holder.tvRole.setVisibility(View.GONE);
            }
        }

        // check to show leave button
        if (holder.ibLeave != null) {
            holder.ibLeave.setVisibility(item.isChecked() ? View.VISIBLE : View.GONE);
        }

        // add long click listener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showLeaveIcon(position);
                return true;
            }
        });

        // check to add leave click listener
        if (holder.ibLeave != null) {
            holder.ibLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeaveTeam(position);
                }
            });
        }
    }

    private void showLeaveIcon(int position) {
        // check old item position
        if (checkedItemPosition != -1) {
            // uncheck old item
            Team checkedTeam = data.get(checkedItemPosition);
            checkedTeam.setChecked(false);
        }

        // check new item
        Team team = data.get(position);
        team.setChecked(true);
        checkedItemPosition = position;
        notifyDataSetChanged();
    }

    private void onLeaveTeam(final int position) {
        // get objects
        Team team = data.get(position);
        User user = userController.getUser();

        // check user role
        if (teamController.isCaptain(team, user.getId())) {
            showChooseNewCaptainDialog(position);
        } else {
            // show the remove confirm dialog with the himself msg
            showRemoveConfirmDialog(position);
        }
    }

    private void showChooseNewCaptainDialog(final int position) {
        // get team item
        Team team = data.get(position);

        // create & show choose player dialog
        ChoosePlayerDialog dialog = new ChoosePlayerDialog(context, team.getId());
        dialog.setRemoveCurrentUser(true);
        dialog.setEmptyMsg(getString(R.string.no_players_in_this_team_except_you));
        dialog.setOnUserSelectedListener(new OnUserSelectedListener() {
            @Override
            public void onUserSelected(User user) {
                showChangeCaptainConfirmDialog(position, user);
            }
        });

        dialog.show();
        Utils.showShortToast(context, R.string.you_must_choose_new_captain_before_continue);
    }

    private void showChangeCaptainConfirmDialog(final int position, final User newCaptain) {
        // show change confirm dialog
        String msg = context.getString(R.string.choose_x_as_new_captain_and_continue_q, newCaptain.getName());
        DialogUtils.showConfirmDialog(context, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCaptain(position, newCaptain, true);
            }
        }, null);
    }

    private void changeCaptain(final int position, final User newCaptain, final boolean leaveTeamAfterChanging) {
        // get team item
        final Team team = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // check if the new captain is an assistant
                    if (teamController.isAssistant(team, newCaptain.getId())) {
                        // remove the team assistant
                        team.setAsstent(null);
                    }
                    // set the new captain
                    team.setCaptain(newCaptain);

                    // update the ui and show msg
                    notifyDataSetChanged();
                    Utils.showShortToast(context, R.string.captain_changed_successfully);

                    // check leave team flag
                    if (leaveTeamAfterChanging) {
                        // leave the team
                        leaveTeam(position);
                    }
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_changing_captain);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_changing_captain);
            }
        };

        // send request
        User user = userController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.changeCaptain(context, connectionListener, user.getId(),
                user.getToken(), team.getId(), newCaptain.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    private void showRemoveConfirmDialog(final int position) {
        // create and show confirm dialog
        DialogUtils.showConfirmDialog(context, R.string.leave_team_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leaveTeam(position);
            }
        }, null);
    }

    private void leaveTeam(final int position) {
        // get objects
        Team team = data.get(position);
        final User user = userController.getUser();

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener connectionListener = new ConnectionListener() {
            @Override
            public void onSuccess(Object response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // remove from adapter and show msg
                    removeItem(position);
                    Utils.showShortToast(context, R.string.leaved_team_successfully);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.failed_leaving_team);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_leaving_team);
            }
        };

        // send request
        ConnectionHandler connectionHandler = ApiRequests.leaveTeam(context, connectionListener, user.getId(),
                user.getToken(), user.getName(), team.getId(), team.getName());
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivLogo;
        private ImageButton ibLeave;
        private TextView tvRole;
        private TextView tvTitle;
        private TextView tvCaptainName;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivLogo = (ImageView) findViewById(R.id.iv_logo);
            ibLeave = (ImageButton) findViewById(R.id.ib_leave);
            tvRole = (TextView) findViewById(R.id.tv_role);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvCaptainName = (TextView) findViewById(R.id.tv_captain_name);
        }
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
