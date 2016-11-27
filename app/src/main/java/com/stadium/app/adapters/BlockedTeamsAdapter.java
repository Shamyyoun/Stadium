package com.stadium.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by karam on 8/13/16.
 */
public class BlockedTeamsAdapter extends ParentRecyclerAdapter<Team> {
    private ActiveUserController userController;

    public BlockedTeamsAdapter(Context context, List<Team> data, int layoutId) {
        super(context, data, layoutId);
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

        // set basic data
        final Team item = data.get(position);
        holder.tvBlockCount.setText("" + item.getBlockTimes());
        holder.tvPlayersCount.setText(context.getString(R.string.contains_x_players, item.getNumberOfPlayers()));

        // set name
        String name = item.getName();
        if (!Utils.isNullOrEmpty(name)) {
            holder.tvName.setText(name);
        } else {
            holder.tvName.setText("-----------");
        }

        // set captain name
        String captainName = null;
        if (item.getCaptain() != null) {
            captainName = item.getCaptain().getName();
        }
        if (!Utils.isNullOrEmpty(captainName)) {
            holder.tvCaptainName.setText(captainName);
        } else {
            holder.tvCaptainName.setText("---------");
        }

        // add click listener
        holder.btnCancelBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelConfirmDialog(position);
            }
        });
    }

    private void showCancelConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.cancel_block_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelBlock(position);
            }
        }, null);
    }

    private void cancelBlock(final int position) {
        // get the team
        final Team team = data.get(position);

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // create the connection listener
        ConnectionListener<String> listener = new ConnectionListener<String>() {
            @Override
            public void onSuccess(String response, int statusCode, String tag) {
                hideProgressDialog();

                // check status code
                if (statusCode == Const.SER_CODE_200) {
                    // show success msg and remove it
                    Utils.showShortToast(context, R.string.cancelled_successfully);
                    removeItem(position);
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

        // prepare request params
        User user = userController.getUser();
        int stadiumId = user.getAdminStadium().getId();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.unblockTeam(context, listener,
                user.getId(), user.getToken(), stadiumId, team.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvBlockCount;
        private TextView tvCaptainName;
        private TextView tvPlayersCount;
        private Button btnCancelBlock;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvBlockCount = (TextView) findViewById(R.id.tv_block_count);
            tvCaptainName = (TextView) findViewById(R.id.tv_captain_name);
            tvPlayersCount = (TextView) findViewById(R.id.tv_players_count);
            btnCancelBlock = (Button) findViewById(R.id.btn_cancel_block);
        }
    }
}
