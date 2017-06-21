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
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.interfaces.OnInvitationAcceptedListener;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class InvitationsAdapter extends ParentRecyclerAdapter<Team> {
    private OnInvitationAcceptedListener invitationAcceptedListener;
    private ActiveUserController activeUserController;

    public InvitationsAdapter(Context context, List<Team> data, int layoutId) {
        super(context, data, layoutId);

        // create user controller
        activeUserController = new ActiveUserController(context);
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

        // set title
        final Team item = data.get(position);
        holder.tvTitle.setText(item.getName());

        // load image
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);

        // add listeners
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAcceptConfirmDialog(position);
            }
        });
    }

    private void showAcceptConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.confirm_invitation_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirm(position);
            }
        }, null);
    }

    private void confirm(final int position) {
        // get the item
        final Team team = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener<Boolean> listener = new ConnectionListener<Boolean>() {
            @Override
            public void onSuccess(Boolean response, int statusCode, String tag) {
                hideProgressDialog();

                // check response
                if (Utils.checkBoolean(response)) {
                    // remove this item and show success msg
                    Utils.showShortToast(context, R.string.confirmed_successfully);
                    removeItem(position);

                    // then fire the invitations accepted listener
                    fireInvitationAcceptedListener(team);
                } else {
                    // show error msg
                    String errorMsg = AppUtils.getResponseMsg(context, response, R.string.error_confirming);
                    Utils.showShortToast(context, errorMsg);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.error_confirming);
            }
        };

        // prepare request params
        User user = activeUserController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.acceptInvitation(context, listener, user.getId(),
                user.getToken(), user.getName(), team.getId(), team.getName());
        cancelWhenDestroyed(connectionHandler);
    }

    private void fireInvitationAcceptedListener(Team team) {
        if (invitationAcceptedListener != null) {
            invitationAcceptedListener.onInvitationAccepted(team);
        }
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivImage;
        private TextView tvTitle;
        private Button btnConfirm;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            btnConfirm = (Button) findViewById(R.id.btn_confirm);
        }
    }

    public void setOnInvitationAcceptedListener(OnInvitationAcceptedListener invitationAcceptedListener) {
        this.invitationAcceptedListener = invitationAcceptedListener;
    }
}
