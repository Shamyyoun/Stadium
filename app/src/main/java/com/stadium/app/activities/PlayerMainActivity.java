package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.fragments.MyTeamsFragment;
import com.stadium.app.fragments.PlayerHomeFragment;
import com.stadium.app.fragments.PlayerReservationsFragment;
import com.stadium.app.fragments.PlayersFragment;
import com.stadium.app.fragments.StadiumsFragment;

public class PlayerMainActivity extends MainActivity {

    private TextView tvHome;
    private TextView tvStadiums;
    private TextView tvReservations;
    private TextView tvPlayers;
    private TextView tvMyTeam;

    private PlayerHomeFragment homeFragment;
    private StadiumsFragment stadiumsFragment;
    private PlayerReservationsFragment reservationsFragment;
    private PlayersFragment playersFragment;
    private MyTeamsFragment myTeamsFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_player_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init tabs
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvStadiums = (TextView) findViewById(R.id.tv_stadiums);
        tvReservations = (TextView) findViewById(R.id.tv_reservations);
        tvPlayers = (TextView) findViewById(R.id.tv_players);
        tvMyTeam = (TextView) findViewById(R.id.tv_my_teams);

        // add tabs click listeners
        tvHome.setOnClickListener(this);
        tvStadiums.setOnClickListener(this);
        tvReservations.setOnClickListener(this);
        tvPlayers.setOnClickListener(this);
        tvMyTeam.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // select home tab by default
        selectTab(tvHome);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
            case R.id.tv_stadiums:
            case R.id.tv_reservations:
            case R.id.tv_players:
            case R.id.tv_my_teams:
                selectTab(v);
                break;

            default:
                super.onClick(v);
        }
    }

    private void selectTab(View tvTab) {
        // deselect all tabs first
        tvHome.setSelected(false);
        tvStadiums.setSelected(false);
        tvReservations.setSelected(false);
        tvPlayers.setSelected(false);
        tvMyTeam.setSelected(false);

        // switch to load the tab fragment
        switch (tvTab.getId()) {
            case R.id.tv_home:
                if (homeFragment == null) {
                    homeFragment = new PlayerHomeFragment();
                }
                loadFragment(R.id.container, homeFragment);
                break;

            case R.id.tv_stadiums:
                if (stadiumsFragment == null) {
                    stadiumsFragment = new StadiumsFragment();
                }
                loadFragment(R.id.container, stadiumsFragment);
                break;

            case R.id.tv_reservations:
                if (reservationsFragment == null) {
                    reservationsFragment = new PlayerReservationsFragment();
                }
                loadFragment(R.id.container, reservationsFragment);
                break;

            case R.id.tv_players:
                if (playersFragment == null) {
                    playersFragment = new PlayersFragment();
                }
                loadFragment(R.id.container, playersFragment);
                break;

            case R.id.tv_my_teams:
                if (myTeamsFragment == null) {
                    myTeamsFragment = new MyTeamsFragment();
                }
                loadFragment(R.id.container, myTeamsFragment);
                break;
        }

        // select the tab
        tvTab.setSelected(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // check refresh home flag
        boolean refreshHome = intent.getBooleanExtra(Const.KEY_REFRESH_HOME, false);
        if (refreshHome) {
            // goto home
            selectTab(tvHome);

            // refresh
            refreshHome();
        }
    }

    private void refreshHome() {
        // refresh after some static time to ensure tab is loaded
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeFragment.refresh();
            }
        }, 300);
    }
}