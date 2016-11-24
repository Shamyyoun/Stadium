package com.stadium.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.models.enums.ReservationsType;
import com.stadium.app.views.SlidingTabLayout;

/*
 * Created by karam on 8/10/16.
 */
public class AdminHomeFragment extends ParentFragment {
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private String[] tabTitles;
    private PagerAdapter pagerAdapter;
    private AdminReservationsFragment todayReservationsFragment;
    private AdminReservationsFragment acceptedReservationsFragment;
    private AdminReservationsFragment newReservationsFragment;
    private AdminReservationsFragment previousReservationsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.home);
        createOptionsMenu(R.menu.menu_admin_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // init views
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // customize view pager & its tab layout
        tabTitles = getResources().getStringArray(R.array.admin_home_tabs);
        tabLayout.setDistributeEvenly(true);
        updatePagerUI();

        return rootView;
    }

    private void updatePagerUI() {
        pagerAdapter = new PagerAdapter(activity.getSupportFragmentManager());
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
            ReservationsType reservationsType = null;

            switch (position) {
                case 0:
                    if (todayReservationsFragment == null) {
                        todayReservationsFragment = new AdminReservationsFragment();
                        reservationsType = ReservationsType.ADMIN_TODAY_RESERVATIONS;
                    }
                    fragment = todayReservationsFragment;

                    break;

                case 1:
                    if (acceptedReservationsFragment == null) {
                        acceptedReservationsFragment = new AdminReservationsFragment();
                        reservationsType = ReservationsType.ADMIN_ACCEPTED_RESERVATIONS;
                    }
                    fragment = acceptedReservationsFragment;

                    break;

                case 2:
                    if (newReservationsFragment == null) {
                        newReservationsFragment = new AdminReservationsFragment();
                        reservationsType = ReservationsType.ADMIN_NEW_RESERVATIONS;
                    }
                    fragment = newReservationsFragment;

                    break;

                case 3:
                    if (previousReservationsFragment == null) {
                        previousReservationsFragment = new AdminReservationsFragment();
                        reservationsType = ReservationsType.ADMIN_PREVIOUS_RESERVATIONS;
                    }
                    fragment = previousReservationsFragment;

                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_RESERVATIONS_TYPE, reservationsType);
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

