package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.TeamController;
import com.stadium.app.fragments.TeamPlayersFragment;
import com.stadium.app.fragments.TeamReservationsFragment;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.Utils;
import com.stadium.app.views.SlidingTabLayout;

/**
 * Created by karam on 7/26/16.
 */
public class TeamInfoActivity extends ParentActivity {
    private int id;
    private ActiveUserController userController;
    private TeamController teamController;

    private ImageView ivImage;
    private TextView tvName;
    private TextView tvPlayersCount;
    private TextView tvStadium;
    private TextView tvResNums;
    private TextView tvDesc;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fabAdd;

    private String[] tabTitles;
    private PagerAdapter pagerAdapter;
    private TeamReservationsFragment reservationsFragment;
    private TeamPlayersFragment playersFragment;

    private boolean enableControls = true;
    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        enableBackButton();

        // create main objects
        id = getIntent().getIntExtra(Const.KEY_ID, 0);
        userController = new ActiveUserController(this);
        teamController = new TeamController();

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPlayersCount = (TextView) findViewById(R.id.tv_players_count);
        tvStadium = (TextView) findViewById(R.id.tv_stadium);
        tvResNums = (TextView) findViewById(R.id.tv_res_nums);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);

        // customize view pager & its tab layout
        tabTitles = getResources().getStringArray(R.array.team_info_tabs);
        tabLayout.setDistributeEvenly(true);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        updatePagerUI();

        // add listeners
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });

        loadTeamInfo();
    }

    private void updatePagerUI() {
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        // select last tab by default as the most right one
        viewPager.setCurrentItem(tabTitles.length - 1);
    }

    private void updateTeamUI() {
        // set basic info
        tvName.setText(team.getName());
        tvPlayersCount.setText(getString(R.string.dot_x_player, team.getNumberOfPlayers()));
        tvResNums.setText(team.getTotalRes() + " / " + team.getAbsentRes());
        tvDesc.setText(team.getDescription());

        // set the stadium name
        if (!Utils.isNullOrEmpty(team.getPreferStadiumName())) {
            tvStadium.setText(team.getPreferStadiumName());
        } else {
            tvStadium.setVisibility(View.GONE);
        }

        // load the image
        Utils.loadImage(this, team.getImageLink(), R.drawable.default_image, ivImage);

        // check the active user role to set his actions
        User user = userController.getUser();
        if (teamController.isCaptain(team, user.getId())
                || teamController.isAssistant(team, user.getId())) {
            // user is a captain or an assistant
            // show add fab and the toolbar menu
            fabAdd.setVisibility(View.VISIBLE);
            createOptionsMenu(R.menu.menu_team_info);
        } else {
            // not a captain or an assistant
            // hide add fab and the toolbar menu
            fabAdd.setVisibility(View.GONE);
            removeOptionsMenu();
        }
    }

    private void onAdd() {
        // switch the current page
        Intent intent = null;
        int requestCode = 0;
        switch (viewPager.getCurrentItem()) {
            case 0:
                intent = new Intent(this, StadiumsActivity.class);
                requestCode = Const.REQ_ADD_RESERVATIONS;
                break;

            case 1:
                intent = new Intent(this, PlayersActivity.class);
                requestCode = Const.REQ_ADD_PLAYERS;
                break;
        }

        // check the intent
        if (intent != null) {
            intent.putExtra(Const.KEY_TEAM, team);
            startActivityForResult(intent, requestCode);
        }
    }

    private void loadTeamInfo() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            disableControls();
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getTeamInfo(this, this, id);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.API_GET_TEAM_INFO.equals(tag)) {
            Team team = (Team) response;

            // check result
            if (statusCode == Const.SER_CODE_200 && team != null) {
                this.team = team;
                updateTeamUI();

                // load data in fragments
                updatePagerUI();
                playersFragment.loadData();
                reservationsFragment.loadData();

                // enable the controls
                enableControls();
            } else {
                // get and show error msg
                String errorMsg = AppUtils.getResponseError(this, team);
                if (errorMsg == null) {
                    errorMsg = getString(R.string.failed_loading_info);
                }
                Utils.showShortToast(this, errorMsg);

                // disable the controls
                disableControls();
            }
        } else {
            super.onSuccess(response, statusCode, tag);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();

        if (Const.API_GET_TEAM_INFO.equals(tag)) {
            super.onFail(ex, statusCode, tag);
            disableControls();
        } else {
            super.onFail(ex, statusCode, tag);
        }
    }

    private void disableControls() {
        fabAdd.setEnabled(false);
        enableControls = false;
    }

    private void enableControls() {
        fabAdd.setEnabled(true);
        enableControls = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_update) {
            if (enableControls) {
                openUpdateTeamActivity();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openUpdateTeamActivity() {
        Intent intent = new Intent(this, UpdateTeamActivity.class);
        intent.putExtra(Const.KEY_TEAM, team);
        startActivityForResult(intent, Const.REQ_UPDATE_TEAM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_UPDATE_TEAM && resultCode == RESULT_OK) {
            // get the new team obj
            Team newTeam = (Team) data.getSerializableExtra(Const.KEY_TEAM);

            // check if user updated the captain or the assistant
            boolean sameCaptainAndAssistant = false;
            if (teamController.isSameCaptain(team, newTeam)
                    && teamController.isSameAssistant(team, newTeam)) {
                sameCaptainAndAssistant = true;
            }

            // update UIs
            this.team = newTeam;
            updateTeamUI();
            if (!sameCaptainAndAssistant) {
                // refresh fragments
                refreshPlayersFragment();
                refreshReservationsFragment();
            }
        } else if (requestCode == Const.REQ_ADD_PLAYERS && resultCode == RESULT_OK) {
            // refresh players fragment
            refreshPlayersFragment();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshPlayersFragment() {
        if (playersFragment != null) {
            playersFragment.updateTeam(team);
            playersFragment.refresh();
        }
    }

    private void refreshReservationsFragment() {
        if (reservationsFragment != null) {
            reservationsFragment.updateTeam(team);
            reservationsFragment.refresh();
        }
    }

    /**
     * used to do suitable logic when the captain changed from players fragment
     */
    public void onCaptainChanged(User newCaptain) {
        team.setCaptain(newCaptain);
        updateTeamUI();
    }

    /**
     * used to do suitable logic when the captain changed from players fragment
     */
    public void onAssistantChanged(User newAssistant) {
        team.setAsstent(newAssistant);
        updateTeamUI();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_TEAM, team);

            switch (position) {
                case 0:
                    if (reservationsFragment == null) {
                        reservationsFragment = new TeamReservationsFragment();
                    }
                    fragment = reservationsFragment;

                    break;

                case 1:
                    if (playersFragment == null) {
                        playersFragment = new TeamPlayersFragment();
                    }
                    fragment = playersFragment;

                    break;
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}