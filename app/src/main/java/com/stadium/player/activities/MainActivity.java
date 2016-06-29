package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stadium.player.R;

public class MainActivity extends ParentToolbarActivity {
    private static int DRAWER_GRAVITY = Gravity.RIGHT;
    private DrawerLayout drawerLayout;
    private TextView tvHome;
    private TextView tvStadiums;
    private TextView tvReservations;
    private TextView tvPlayers;
    private TextView tvMyTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // customize the toolbar
        setToolbarIcon(R.drawable.menu_icon);

        // customize the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // init tabs
        tvHome = (TextView) findViewById(R.id.tv_home);
        tvStadiums = (TextView) findViewById(R.id.tv_stadiums);
        tvReservations = (TextView) findViewById(R.id.tv_reservations);
        tvPlayers = (TextView) findViewById(R.id.tv_players);
        tvMyTeam = (TextView) findViewById(R.id.tv_my_team);

        // add tabs click listeners
        tvHome.setOnClickListener(this);
        tvStadiums.setOnClickListener(this);
        tvReservations.setOnClickListener(this);
        tvPlayers.setOnClickListener(this);
        tvMyTeam.setOnClickListener(this);

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
            case R.id.tv_my_team:
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
                logE("Load Home");
                break;

            case R.id.tv_stadiums:
                logE("Load Stadiums");
                break;

            case R.id.tv_reservations:
                logE("Load Reservations");
                break;

            case R.id.tv_players:
                logE("Load Players");
                break;

            case R.id.tv_my_team:
                logE("Load My Team");
                break;
        }

        // select the tab
        tvTab.setSelected(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onMenuIcon();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuIcon() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeMenuDrawer();
        } else {
            drawerLayout.openDrawer(DRAWER_GRAVITY);
        }
    }

    public void closeMenuDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            closeMenuDrawer();
        } else {
            super.onBackPressed();
        }
    }
}