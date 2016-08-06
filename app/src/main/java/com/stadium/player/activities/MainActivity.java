package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.fragments.HomeFragment;
import com.stadium.player.fragments.MyTeamsFragment;
import com.stadium.player.fragments.PlayersFragment;
import com.stadium.player.fragments.ReservationsFragment;
import com.stadium.player.fragments.StadiumsFragment;

public class MainActivity extends ParentToolbarActivity {

    private static int DRAWER_GRAVITY = Gravity.RIGHT;
    private DrawerLayout drawerLayout;
    private TextView tvHome;
    private TextView tvStadiums;
    private TextView tvReservations;
    private TextView tvPlayers;
    private TextView tvMyTeam;

    private HomeFragment homeFragment;
    private StadiumsFragment stadiumsFragment;
    private ReservationsFragment reservationsFragment;
    private PlayersFragment playersFragment;
    private MyTeamsFragment myTeamsFragment;

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
                    homeFragment = new HomeFragment();
                }
                loadFragment(R.id.container_main, homeFragment);
                break;

            case R.id.tv_stadiums:
                if (stadiumsFragment == null) {
                    stadiumsFragment = new StadiumsFragment();
                }
                loadFragment(R.id.container_main, stadiumsFragment);
                break;

            case R.id.tv_reservations:
                if (reservationsFragment == null) {
                    reservationsFragment = new ReservationsFragment();
                }
                loadFragment(R.id.container_main, reservationsFragment);
                break;

            case R.id.tv_players:
                if (playersFragment == null) {
                    playersFragment = new PlayersFragment();
                }
                loadFragment(R.id.container_main, playersFragment);
                break;

            case R.id.tv_my_teams:
                if (myTeamsFragment == null) {
                    myTeamsFragment = new MyTeamsFragment();
                }
                loadFragment(R.id.container_main, myTeamsFragment);
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