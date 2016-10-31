package com.stadium.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.stadium.app.controllers.UserController;
import com.stadium.app.dialogs.ChooseTeamDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class PlayersAdapter extends ParentRecyclerAdapter<User> {
    private ActiveUserController activeUserController;
    private UserController userController;
    private boolean isSimpleView;
    private ChooseTeamDialog teamsDialog;

    public PlayersAdapter(Context context, List<User> data, int layoutId) {
        super(context, data, layoutId);

        // obtain main objects
        activeUserController = new ActiveUserController(context);
        isSimpleView = (layoutId == R.layout.item_player_simple);
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

        // load the image as it is shared among all views
        final User item = data.get(position);
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);

        // check if simple view
        if (isSimpleView) {
            // set simple name
            holder.tvName.setText(item.getName());
        } else {
            // set basic data
            userController = new UserController(item);
            holder.tvName.setText(userController.getNamePosition());
            holder.rbRating.setRating((float) item.getRate());

            // set address
            String address = userController.getCityName();
            if (address != null) {
                holder.tvAddress.setText(address);
                holder.tvAddress.setVisibility(View.VISIBLE);
            } else {
                holder.tvAddress.setVisibility(View.GONE);
            }

            // create global click listener
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.layout_content:
                            openPlayerInfo(position);
                            break;

                        case R.id.ib_add:
                            chooseTeam(position);
                            break;
                    }
                }
            };

            // add listeners
            holder.layoutContent.setOnClickListener(clickListener);
            holder.ibAdd.setOnClickListener(clickListener);
        }
    }

    private void openPlayerInfo(int position) {
        User user = data.get(position);
        Intent intent = new Intent(context, PlayerInfoActivity.class);
        intent.putExtra(Const.KEY_ID, user.getId());
        context.startActivity(intent);
    }

    private void chooseTeam(final int position) {
        if (teamsDialog == null) {
            teamsDialog = new ChooseTeamDialog(context);
            teamsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Team team = (Team) item;
                    addPlayerToTeam(position, team);
                }
            });
        }

        teamsDialog.show();
    }

    private void addPlayerToTeam(int position, Team team) {
        // get the player
        User player = data.get(position);

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

                // check result
                if (statusCode == Const.SER_CODE_200) {
                    Utils.showShortToast(context, R.string.added_successfully);
                } else {
                    Utils.showShortToast(context, R.string.failed_adding_player);
                }
            }

            @Override
            public void onFail(Exception ex, int statusCode, String tag) {
                hideProgressDialog();
                Utils.showShortToast(context, R.string.failed_adding_player);
            }
        };

        // send request
        User user = activeUserController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.addMemberToTeam(context, connectionListener,
                user.getId(), user.getToken(), team.getId(), team.getName(), player.getId(), player.getName());
        cancelWhenDestroyed(connectionHandler);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvAddress;
        private RatingBar rbRating;
        private ImageButton ibAdd;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvAddress = (TextView) findViewById(R.id.tv_address);
            rbRating = (RatingBar) findViewById(R.id.rb_rating);
            ibAdd = (ImageButton) findViewById(R.id.ib_add);
        }
    }
}
