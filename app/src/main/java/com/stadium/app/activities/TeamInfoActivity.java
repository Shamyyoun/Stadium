package com.stadium.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.stadium.app.R;
import com.stadium.app.fragments.TeamPlayersFragment;
import com.stadium.app.fragments.TeamReservationsFragment;
import com.stadium.app.views.SlidingTabLayout;

/**
 * Created by karam on 7/26/16.
 */
public class TeamInfoActivity extends ParentActivity {
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private String[] tabTitles;
    private PagerAdapter pagerAdapter;
    private TeamReservationsFragment reservationsFragment;
    private TeamPlayersFragment playersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        // customize toolbar
        createOptionsMenu(R.menu.menu_team_info);
        enableBackButton();

        // init views
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // customize view pager & its tab layout
        tabTitles = getResources().getStringArray(R.array.team_info_tabs);
        tabLayout.setDistributeEvenly(true);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        // select last tab by default as the most right one
        viewPager.setCurrentItem(tabTitles.length - 1);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

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