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
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.TeamController;
import com.stormnology.stadium.controllers.UserController;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/26/16.
 */
public class TeamInvitationsAdapter extends ParentRecyclerAdapter<User> {
    private Team team;
    private ActiveUserController activeUserController;
    private TeamController teamController;


    public TeamInvitationsAdapter(Context context, List<User> data, int layoutId, Team team) {
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

        // set name
        holder.tvName.setText(userController.getNamePosition());

        // set address
        if (userController.getCityName() != null) {
            holder.tvAddress.setText(userController.getCityName());
            holder.tvAddress.setVisibility(View.VISIBLE);
        } else {
            holder.tvAddress.setVisibility(View.GONE);
        }

        // load image
        Utils.loadImage(context, player.getImageLink(), R.drawable.default_image, holder.ivImage);

        // check active user role to show / hide buttons layout
        User activeUser = activeUserController.getUser();
        if (teamController.isAssistant(team, activeUser.getId())
                || teamController.isCaptain(team, activeUser.getId())) {
            holder.layoutButtons.setVisibility(View.VISIBLE);
        } else if (!teamController.isCaptain(team, activeUser.getId())) {
            holder.layoutButtons.setVisibility(View.GONE);
        }

        // create the global click listener
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.layout_content:
                        openPlayerInfoActivity(player.getId());
                        break;

                    case R.id.btn_cancel_invitation:
                        showCancelInvitationConfirmDialog(position);
                        break;
                }
            }
        };

        // add listeners
        holder.layoutContent.setOnClickListener(clickListener);
        holder.btnCancelInvitation.setOnClickListener(clickListener);
    }

    private void openPlayerInfoActivity(int playerId) {
        Intent intent = new Intent(context, PlayerInfoActivity.class);
        intent.putExtra(Const.KEY_ID, playerId);
        context.startActivity(intent);
    }

    private void showCancelInvitationConfirmDialog(final int position) {
        // create and show confirm dialog
        DialogUtils.showConfirmDialog(context, R.string.cancel_invitation_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelInvitation(position);
            }
        }, null);
    }

    private void cancelInvitation(final int position) {
        // get the user
        final User player = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener<Boolean> connectionListener = new ConnectionListener<Boolean>() {
            @Override
            public void onSuccess(Boolean response, int statusCode, String tag) {
                hideProgressDialog();

                // check response
                if (Utils.checkBoolean(response)) {
                    // remove from adapter and show msg
                    removeItem(position);
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

        // get the active user
        User user = activeUserController.getUser();

        // create & send the request
        ConnectionHandler connectionHandler = ApiRequests.removeInvitation(context, connectionListener,
                user.getId(), user.getToken(), team.getId(), player.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvAddress;
        private View layoutButtons;
        private Button btnCancelInvitation;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvAddress = (TextView) findViewById(R.id.tv_address);
            layoutButtons = findViewById(R.id.layout_buttons);
            btnCancelInvitation = (Button) findViewById(R.id.btn_cancel_invitation);
        }
    }
}