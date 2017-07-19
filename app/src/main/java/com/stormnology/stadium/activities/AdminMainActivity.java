package com.stormnology.stadium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.fragments.AdminHomeFragment;
import com.stormnology.stadium.fragments.AdminStadiumFragment;
import com.stormnology.stadium.fragments.BlockedTeamsFragment;
import com.stormnology.stadium.fragments.RepeatedReservationFragment;
import com.stormnology.stadium.fragments.StadiumsFragment;

public class AdminMainActivity extends MainActivity {

    private TextView tvHome;
    private TextView tvStadiums;
    private TextView tvStadium;
    private TextView tvBlocked;
    private TextView tvRepeated;

    private AdminHomeFragment homeFragment;
    private StadiumsFragment stadiumsFragment;
    private AdminStadiumFragment stadiumFragment;
    private BlockedTeamsFragment blockedFragment;
    private RepeatedReservationFragment repeatedFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_admin_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init tabs
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvStadiums = (TextView) findViewById(R.id.tv_stadiums);
        tvStadium = (TextView) findViewById(R.id.tv_stadium);
        tvBlocked = (TextView) findViewById(R.id.tv_blocked);
        tvRepeated = (TextView) findViewById(R.id.tv_repeated);

        // add tabs click listeners
        tvHome.setOnClickListener(this);
        tvStadiums.setOnClickListener(this);
        tvStadium.setOnClickListener(this);
        tvBlocked.setOnClickListener(this);
        tvRepeated.setOnClickListener(this);
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
            case R.id.tv_stadium:
            case R.id.tv_blocked:
            case R.id.tv_repeated:
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
        tvStadium.setSelected(false);
        tvBlocked.setSelected(false);
        tvRepeated.setSelected(false);

        // switch to load the tab fragment
        switch (tvTab.getId()) {
            case R.id.tv_home:
                // TODO must be refactored in a better way
                homeFragment = new AdminHomeFragment();
                loadFragment(R.id.container, homeFragment);
                break;

            case R.id.tv_stadiums:
                if (stadiumsFragment == null) {
                    stadiumsFragment = new StadiumsFragment();
                }
                loadFragment(R.id.container, stadiumsFragment);
                break;

            case R.id.tv_stadium:
                // TODO must be refactored in a better way
                stadiumFragment = new AdminStadiumFragment();
                loadFragment(R.id.container, stadiumFragment);
                break;

            case R.id.tv_blocked:
                if (blockedFragment == null) {
                    blockedFragment = new BlockedTeamsFragment();
                }
                loadFragment(R.id.container, blockedFragment);
                break;

            case R.id.tv_repeated:
                if (repeatedFragment == null) {
                    repeatedFragment = new RepeatedReservationFragment();
                }
                loadFragment(R.id.container, repeatedFragment);
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
            // just goto home
            selectTab(tvHome);
        }
    }
}