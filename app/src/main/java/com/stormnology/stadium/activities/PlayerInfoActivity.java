package com.stormnology.stadium.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.TeamsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.UserController;
import com.stormnology.stadium.dialogs.ChooseFromCaptainTeamsDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.interfaces.OnItemClickListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.utils.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 7/19/16.
 */
public class PlayerInfoActivity extends ParentActivity {
    private int id;
    private ActiveUserController userController;

    private ImageView ivImage;
    private TextView tvRating;
    private TextView tvName;
    private TextView tvCity;
    private TextView tvBio;
    private RatingBar rbRating;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private ProgressBar pbTeamsProgress;
    private TextView tvTeamsEmpty;
    private TextView tvTeamsError;

    private User player;
    private List<Team> teams;
    private TeamsAdapter teamsAdapter;
    private ChooseFromCaptainTeamsDialog teamsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);
        enableBackButton();

        // obtain main objects
        id = getIntent().getIntExtra(Const.KEY_ID, 0);
        userController = new ActiveUserController(this);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvBio = (TextView) findViewById(R.id.tv_bio);
        rbRating = (RatingBar) findViewById(R.id.rb_rating);
        btnAdd = (Button) findViewById(R.id.btn_add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pbTeamsProgress = (ProgressBar) findViewById(R.id.pb_teams_progress);
        tvTeamsEmpty = (TextView) findViewById(R.id.tv_teams_empty);
        tvTeamsError = (TextView) findViewById(R.id.tv_teams_error);

        // customize the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);

        // add listeners
        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    showRateDialog(rating);
                }
            }
        });
        ivImage.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        loadUserInfo();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_image) {
            openViewImageActivity();
        } else if (v.getId() == R.id.btn_add) {
            chooseTeam();
        } else {
            super.onClick(v);
        }
    }

    private void openViewImageActivity() {
        Intent intent = new Intent(this, ViewImageActivity.class);
        intent.putExtra(Const.KEY_IMAGE_URL, player.getImageLink());
        startActivity(intent);
        overridePendingTransition(R.anim.scale_fade_enter, R.anim.scale_fade_exit);
    }

    private void showRateDialog(final double rate) {
        String msg = getString(R.string.rate_this_player_with_x_starts_q, Utils.formatDouble(rate));
        DialogUtils.showConfirmDialog(this, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ratePlayer(rate);
            }
        }, null);
    }

    private void chooseTeam() {
        if (teamsDialog == null) {
            teamsDialog = new ChooseFromCaptainTeamsDialog(this);
            teamsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Team team = (Team) item;
                    addPlayerToTeam(team);
                }
            });
        }

        teamsDialog.show();
    }

    private void updateUserUI() {
        // set basic info
        UserController userController = new UserController(player);
        tvName.setText(userController.getNamePosition());
        tvRating.setText(Utils.formatDouble(player.getRate()));
        tvBio.setText(player.getBio());

        // set city
        if (!Utils.isNullOrEmpty(userController.getCityName())) {
            tvCity.setText(userController.getCityName());
        } else {
            tvCity.setText("-----");
        }

        // load the profile image
        Utils.loadImage(activity, player.getImageLink(), R.drawable.default_image, ivImage);
    }

    private void updateTeamsUI() {
        // set the adapter
        teamsAdapter = new TeamsAdapter(this, teams, R.layout.item_team_simple);
        teamsAdapter.setPlayerId(id);
        recyclerView.setAdapter(teamsAdapter);
        showTeamsMain();

        // add item click listener
        teamsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open team info activity
                Team team = teams.get(position);
                Intent intent = new Intent(activity, TeamInfoActivity.class);
                intent.putExtra(Const.KEY_ID, team.getId());
                startActivity(intent);
            }
        });
    }

    private void loadUserInfo() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            enableControls(false);
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getPlayerInfo(this, this, id);
        cancelWhenDestroyed(connectionHandler);
    }

    private void refreshUserTeams() {
        loadUserTeams();
    }

    private void loadUserTeams() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showTeamsProgress();

        // send request
        User user = userController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.listOfMyTeams(this, this, user.getToken(), id);
        cancelWhenDestroyed(connectionHandler);
    }

    private void ratePlayer(double rate) {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // send request
        User activeUser = userController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.ratePlayer(this, this, activeUser.getId(),
                activeUser.getToken(), activeUser.getName(), player.getId(), rate);
        cancelWhenDestroyed(connectionHandler);
    }

    private void addPlayerToTeam(Team team) {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // send request
        User user = userController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.addMemberToTeam(this, this, user.getId(),
                user.getToken(), team.getId(), team.getName(), id, player.getName());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.API_GET_PLAYER_INFO.equals(tag)) {
            User user = (User) response;

            // check result
            if (statusCode == Const.SER_CODE_200 && user != null) {
                player = user;
                updateUserUI();
                enableControls(true);

                loadUserTeams();
            } else {
                String errorMsg = AppUtils.getResponseMsg(this, user);
                if (errorMsg == null) {
                    errorMsg = getString(R.string.failed_loading_info);
                }

                Utils.showShortToast(this, errorMsg);

                enableControls(false);
            }
        } else if (Const.API_RATE_PLAYER.equals(tag)) {
            // check result
            if (statusCode == Const.SER_CODE_200) {
                // update player rating if possible
                User user = (User) response;
                if (user != null) {
                    player.setRate(user.getRate());
                }

                // show msg
                Utils.showShortToast(this, R.string.rated_successfully);
            } else {
                // show error msg
                String errorMsg = AppUtils.getResponseMsg(this, response, R.string.failed_rating);
                Utils.showShortToast(this, errorMsg);
            }
        } else if (Const.API_ADD_MEMBER_TO_TEAM.equals(tag)) {
            // check result
            if (statusCode == Const.SER_CODE_200) {
                Utils.showShortToast(this, R.string.added_successfully);
                refreshUserTeams();
            } else {
                // show error msg
                String errorMsg = AppUtils.getResponseMsg(this, response, R.string.failed_adding_player);
                Utils.showShortToast(this, errorMsg);
            }
        } else if (Const.API_LIST_OF_MY_TEAMS.equals(tag)) {
            // get data
            Team[] teamsArr = (Team[]) response;
            teams = new ArrayList<>(Arrays.asList(teamsArr));

            // check size
            if (teams.size() == 0) {
                showTeamsEmpty();
            } else {
                updateTeamsUI();
            }
        } else {
            super.onSuccess(response, statusCode, tag);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();

        if (Const.API_LIST_OF_MY_TEAMS.equals(tag)) {
            showTeamsError();
        } else if (Const.API_GET_PLAYER_INFO.equals(tag)) {
            super.onFail(ex, statusCode, tag);
            enableControls(false);
        } else {
            super.onFail(ex, statusCode, tag);
        }
    }

    private void enableControls(boolean enable) {
        ivImage.setEnabled(enable);
        rbRating.setIsIndicator(!enable);
        btnAdd.setEnabled(enable);
    }

    private void showTeamsProgress() {
        ViewUtil.showOneView(pbTeamsProgress, tvTeamsError, recyclerView, tvTeamsEmpty);
    }

    private void showTeamsEmpty() {
        ViewUtil.showOneView(tvTeamsEmpty, pbTeamsProgress, tvTeamsError, recyclerView);
    }

    private void showTeamsError() {
        ViewUtil.showOneView(tvTeamsError, tvTeamsEmpty, pbTeamsProgress, recyclerView);
    }

    private void showTeamsMain() {
        ViewUtil.showOneView(recyclerView, tvTeamsError, tvTeamsEmpty, pbTeamsProgress);
    }

    @Override
    public void onBackPressed() {
        // set player rating as result if possible
        if (player != null) {
            Intent intent = new Intent();
            intent.putExtra(Const.KEY_RATING, player.getRate());
            setResult(RESULT_OK, intent);
        }

        super.onBackPressed();
    }
}
