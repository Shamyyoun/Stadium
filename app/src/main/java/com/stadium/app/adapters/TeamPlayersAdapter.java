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
import com.stadium.app.models.entities.PlayerRole;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

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
        final User item = data.get(position);
        UserController userController = new UserController(item);

        // set basic data
        holder.tvName.setText(userController.getNamePosition());
        holder.rbRating.setRating((float) item.getRate());

        // set address
        if (userController.getCityName() != null) {
            holder.tvAddress.setText(userController.getCityName());
            holder.tvAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddress.setVisibility(View.GONE);
        }

        // set player the role
        PlayerRole role = teamController.getPlayerRole(team, item.getId());
        if (role != null) {
            holder.tvRole.setText(role.getChar());
            holder.tvRole.setBackgroundResource(role.getBackgroundResId());
            holder.tvRole.setVisibility(View.VISIBLE);
        } else {
            holder.tvRole.setVisibility(View.GONE);
        }

        // load image
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);

        // show / hide the action buttons according to active user role
        User activeUser = activeUserController.getUser();
        if (teamController.isAssistant(team, activeUser.getId())) {
            holder.btnRole.setVisibility(View.GONE);
            holder.viewButtonsDivider.setVisibility(View.GONE);
        } else if (!teamController.isCaptain(team, activeUser.getId())) {
            holder.layoutButtons.setVisibility(View.GONE);
        }

        // customize the role button according to player role
        if (teamController.isCaptain(team, item.getId())) {
            holder.btnRole.setText(R.string.delete_captain);
            holder.btnRole.setTextColor(context.getResources().getColor(R.color.red));
            holder.btnRole.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.red_delete_icon, 0, 0, 0);
        } else if (teamController.isAssistant(team, item.getId())) {
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
                        openPlayerInfoActivity(item.getId());
                        break;

                    case R.id.btn_role:
                        onRoleButton(position);
                        break;

                    case R.id.btn_remove:
                        showRemoveDialog(position);
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

        } else if (teamController.isAssistant(team, player.getId())) {
            showMakeAssistantDialog(position, false);
        } else {
            showMakeAssistantDialog(position, true);
        }
    }

    private void showMakeAssistantDialog(final int position, final boolean makeAssistant) {
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

    private void showRemoveDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.remove_from_team_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeFromTeam(position);
            }
        }, null);
    }

    private void removeFromTeam(final int position) {
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
                    // remove this player
                    data.remove(player);
                    notifyItemRemoved(position);

                    // show msg
                    Utils.showShortToast(context, R.string.removed_successfully);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseError(context, response, R.string.failed_removing_player);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_removing_player);
            }
        };

        // send request
        User user = activeUserController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.deleteMemberFromTeam(context, connectionListener, user.getId(),
                user.getToken(), team.getId(), player.getId());
        cancelWhenDestroyed(connectionHandler);
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