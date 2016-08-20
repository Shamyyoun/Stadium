package com.stadium.app.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.fragments.AdminBlockedFragment;
import com.stadium.app.fragments.AdminHomeFragment;
import com.stadium.app.fragments.AdminRepeatedFragment;
import com.stadium.app.fragments.AdminStadeFragment;
import com.stadium.app.fragments.AdminStadiumFragment;

/*
 * Created by karam on 8/10/16.
 */
public class AdminMainActivity extends ParentToolbarActivity {

    private TextView tvHome;
    private TextView tvStadiums;
    private TextView tvStade;
    private TextView tvBlocked;
    private TextView tvRepeated;

    private AdminHomeFragment homeFragment;
    private AdminStadiumFragment stadiumsFragment;
    private AdminStadeFragment stadeFragment;
    private AdminBlockedFragment blockedFragment;
    private AdminRepeatedFragment repeatedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // init tabs
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvStadiums = (TextView) findViewById(R.id.tv_stadiums);
        tvStade = (TextView) findViewById(R.id.tv_stade);
        tvBlocked = (TextView) findViewById(R.id.tv_blocked);
        tvRepeated = (TextView) findViewById(R.id.tv_repeated);

        // add tabs click listeners
        tvHome.setOnClickListener(this);
        tvStadiums.setOnClickListener(this);
        tvStade.setOnClickListener(this);
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
            case R.id.tv_stade:
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
        tvStade.setSelected(false);
        tvBlocked.setSelected(false);
        tvRepeated.setSelected(false);

        // switch to load the tab fragment
        switch (tvTab.getId()) {
            case R.id.tv_home:
                if (homeFragment == null) {
                    homeFragment = new AdminHomeFragment();
                }
                loadFragment(R.id.container_main, homeFragment);
                break;

            case R.id.tv_stadiums:
                if (stadiumsFragment == null) {
                    stadiumsFragment = new AdminStadiumFragment();
                }
                loadFragment(R.id.container_main, stadiumsFragment);
                break;

            case R.id.tv_stade:
                if (stadeFragment == null) {
                    stadeFragment = new AdminStadeFragment();
                }
                loadFragment(R.id.container_main, stadeFragment);
                break;

            case R.id.tv_blocked:
                if (blockedFragment == null) {
                    blockedFragment = new AdminBlockedFragment();
                }
                loadFragment(R.id.container_main, blockedFragment);
                break;

            case R.id.tv_repeated:
                if (repeatedFragment == null) {
                    repeatedFragment = new AdminRepeatedFragment();
                }
                loadFragment(R.id.container_main, repeatedFragment);
                break;
        }

        // select the tab
        tvTab.setSelected(true);
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
