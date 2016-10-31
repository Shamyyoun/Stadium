package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.PlayerInfoActivity;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.TeamController;
import com.stadium.app.controllers.UserController;
import com.stadium.app.dialogs.ChoosePlayerDialog;
import com.stadium.app.interfaces.OnUserSelectedListener;
import com.stadium.app.models.entities.PlayerRole;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/26/16.
 */
public class TeamPlayersAdapter extends ParentRecyclerAdapter<User> {
    private Team team;
    private ActiveUserController activeUserController;
    private TeamController teamController;

    public TeamPlayersAdapter(Context context, List<User> data, int layoutId, Team team) {
        super(context, data, layoutId);
        this.team = team;
        activeUserController = new ActiveUserController(context);
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

        // get item and create the controller
        final User player = data.get(position);
        UserController userController = new UserController(player);

        // set basic data
        holder.tvName.setText(userController.getNamePosition());
        holder.rbRating.setRating((float) player.getRate());

        // set address
        if (userController.getCityName() != null) {
            holder.tvAddress.setText(userController.getCityName());
            holder.tvAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddress.setVisibility(View.GONE);
        }

        // set player the role
        PlayerRole role = teamController.getPlayerRole(team, player.getId());
        if (role != null) {
            holder.tvRole.setText(role.getChar());
            holder.tvRole.setBackgroundResource(role.getBackgroundResId());
            holder.tvRole.setVisibility(View.VISIBLE);
        } else {
            holder.tvRole.setVisibility(View.GONE);
        }

        // load image
        Utils.loadImage(context, player.getImageLink(), R.drawable.default_image, holder.ivImage);

        // show / hide the action buttons according to active user role
        User activeUser = activeUserController.getUser();
        if (teamController.isAssistant(team, activeUser.getId())) {
            // the active user is the assistant of this team
            // check the current team player role in the team
            if (teamController.isCaptain(team, player.getId())) {
                // the assistant has no action for the captain, so
                // hide the buttons layout
                holder.layoutButtons.setVisibility(View.GONE);
            } else {
                // the assistant has the remove option for any other player
                // hide the role button and keep the remove button
                holder.btnRole.setVisibility(View.GONE);
                holder.viewButtonsDivider.setVisibility(View.GONE);
                holder.layoutButtons.setVisibility(View.VISIBLE);
            }
        } else if (!teamController.isCaptain(team, activeUser.getId())) {
            holder.layoutButtons.setVisibility(View.GONE);
        }

        // customize the role button according to player role
        if (teamController.isCaptain(team, player.getId())) {
            holder.btnRole.setText(R.string.delete_captain);
            holder.btnRole.setTextColor(context.getResources().getColor(R.color.red));
            holder.btnRole.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.red_delete_icon, 0, 0, 0);
        } else if (teamController.isAssistant(team, player.getId())) {
            holder.btnRole.setText(R.string.delete_assistant);
            holder.btnRole.setTextColor(context.getResources().getColor(R.color.red));
            holder.btnRole.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.red_delete_icon, 0, 0, 0);
        } else {
            holder.btnRole.setText(R.string.make_assistant);
            holder.btnRole.setTextColor(context.getResources().getColor(R.color.green));
            holder.btnRole.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.green_confirm_icon, 0, 0, 0);
        }

        // create the global click listener
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_content:
                        openPlayerInfoActivity(player.getId());
                        break;

                    case R.id.btn_role:
                        onRoleButton(position);
                        break;

                    case R.id.btn_remove:
                        onRemoveButton(position);
                        break;
                }
            }
        };

        // add listeners
        holder.layoutContent.setOnClickListener(clickListener);
        holder.btnRole.setOnClickListener(clickListener);
        holder.btnRemove.setOnClickListener(clickListener);
    }

    private void openPlayerInfoActivity(int playerId) {
        Intent intent = new Intent(context, PlayerInfoActivity.class);
        intent.putExtra(Const.KEY_ID, playerId);
        context.startActivity(intent);
    }

    private void onRoleButton(int position) {
        User player = data.get(position);
        if (teamController.isCaptain(team, player.getId())) {
            showChooseNewCaptainDialog(position, false);
        } else if (teamController.isAssistant(team, player.getId())) {
            showMakeAssistantConfirmDialog(position, false);
        } else {
            showMakeAssistantConfirmDialog(position, true);
        }
    }

    private void onRemoveButton(final int position) {
        // check if removing himself
        User player = data.get(position);
        if (player.getId() == activeUserController.getUser().getId()) {
            // check role
            if (teamController.isCaptain(team, player.getId())) {
                showChooseNewCaptainDialog(position, true);
            } else {
                // show the remove confirm dialog with the himself msg
                showRemoveConfirmDialog(position, true, R.string.leave_team_q);
            }
        } else {
            // show the remove confirm dialog with the generic msg
            showRemoveConfirmDialog(position, false, R.string.remove_from_team_q);
        }
    }

    private void showChooseNewCaptainDialog(final int position, final boolean removeOld) {
        // prepare the players list
        List<User> players = new ArrayList<>(data);
        players.remove(position);

        ChoosePlayerDialog dialog = new ChoosePlayerDialog(context, players);
        dialog.setCustomEmptyMsg(getString(R.string.no_players_in_this_team_except_you));
        dialog.setOnUserSelectedListener(new OnUserSelectedListener() {
            @Override
            public void onUserSelected(User user) {
                showChangeCaptainConfirmDialog(position, user, removeOld);
            }
        });

        dialog.show();
        Utils.showShortToast(context, R.string.you_must_choose_new_captain_before_continue);
    }

    private void showChangeCaptainConfirmDialog(final int position, final User newCaptain, final boolean removeOld) {
        String msg = context.getString(R.string.choose_x_as_new_captain_and_continue_q, newCaptain.getName());
        DialogUtils.showConfirmDialog(context, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCaptain(position, newCaptain, removeOld);
            }
        }, null);
    }

    private void changeCaptain(final int position, final User newCaptain, final boolean removeOld) {
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

                    // check to remove the captain
                    if (removeOld) {
                        removeFromTeam(position, true);
                    }
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseError(context, response, R.string.failed_changing_captain);
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
        User user = activeUserController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.changeCaptain(context, connectionListener, user.getId(),
                user.getToken(), team.getId(), newCaptain.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    private void showMakeAssistantConfirmDialog(final int position, final boolean makeAssistant) {
        int msgId = makeAssistant ? R.string.make_assistant_q : R.string.delete_assistant_q;
        DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeAssistant(position, makeAssistant);
            }
        }, null);
    }

    private void makeAssistant(final int position, final boolean makeAssistant) {
        // get the player
        final User player = data.get(position);

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
                    // update the assistant
                    if (makeAssistant) {
                        team.setAsstent(player);
                    } else {
                        team.setAsstent(null);
                    }
                    notifyDataSetChanged();

                    // show msg
                    Utils.showShortToast(context, makeAssistant ? R.string.made_successfully : R.string.deleted_successfully);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseError(context, response, makeAssistant ? R.string.failed_making_player : R.string.failed_deleting);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, makeAssistant ? R.string.failed_making_player : R.string.failed_deleting);
            }
        };

        // prepare params
        User user = activeUserController.getUser();
        int playerId = makeAssistant ? player.getId() : 0;

        // send request
        ConnectionHandler connectionHandler = ApiRequests.chooseAssistant(context, connectionListener, user.getId(),
                user.getToken(), team.getId(), playerId);
        cancelWhenDestroyed(connectionHandler);
    }

    private void showRemoveConfirmDialog(final int position, final boolean isCaptain, int msgId) {
        // create and show confirm dialog
        DialogUtils.showConfirmDialog(context, msgId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFromTeam(position, isCaptain);
            }
        }, null);
    }

    private void removeFromTeam(final int position, final boolean isCaptain) {
        // get the player
        final User player = data.get(position);

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
                    Utils.showShortToast(context, isCaptain ? R.string.leaved_team_successfully : R.string.removed_successfully);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseError(context, response, isCaptain ? R.string.failed_leaving_team : R.string.failed_removing);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, isCaptain ? R.string.failed_leaving_team : R.string.failed_removing);
            }
        };

        // get the active user
        User user = activeUserController.getUser();

        // check to send suitable request
        ConnectionHandler connectionHandler;
        if (isCaptain) {
            connectionHandler = ApiRequests.leaveTeam(context, connectionListener, user.getId(),
                    user.getToken(), user.getName(), team.getId(), team.getName());
        } else {
            connectionHandler = ApiRequests.deleteMemberFromTeam(context, connectionListener, user.getId(),
                    user.getToken(), team.getId(), team.getName(), player.getId(), player.getName());
        }

        // cancel the request when destroyed
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void removeItem(int position) {
        data.remove(position);
        notifyDataSetChanged();

        if (itemRemovedListener != null) {
            itemRemovedListener.onItemRemoved(position);
        }
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivImage;
        private TextView tvRole;
        private TextView tvName;
        private TextView tvAddress;
        private RatingBar rbRating;
        private View layoutButtons;
        private View viewButtonsDivider;
        private Button btnRole;
        private Button btnRemove;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvRole = (TextView) findViewById(R.id.tv_role);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvAddress = (TextView) findViewById(R.id.tv_address);
            rbRating = (RatingBar) findViewById(R.id.rb_rating);
            layoutButtons = findViewById(R.id.layout_buttons);
            viewButtonsDivider = findViewById(R.id.view_buttons_divider);
            btnRole = (Button) findViewById(R.id.btn_role);
            btnRemove = (Button) findViewById(R.id.btn_remove);
        }
    }
}