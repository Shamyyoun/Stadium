package com.stormnology.stadium.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.models.enums.ReservationsType;
import com.stormnology.stadium.views.SlidingTabLayout;

/*
 * Created by Shamyyoun on 7/14/17.
 */
public class ChallengesFragment extends ParentFragment {
    private static final int TODAY_RES_POS = 0;
    private static final int ACCEPTED_RES_POS = 1;
    private static final int NEW_RES_POS = 2;
    private static final int PREVIOUS_RES_POS = 3;
    private static final int MY_RES_POS = 4;

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private String[] tabTitles;
    private PagerAdapter pagerAdapter;
    private AdminReservationsFragment[] fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.challenges);
//        removeOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);

//        // init views
//        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
//
//        // customize view pager & its tab layout
//        tabTitles = getResources().getStringArray(R.array.admin_home_tabs);
//        tabLayout.setDistributeEvenly(true);
//        updatePagerUI();

        return rootView;
    }

    private void updatePagerUI() {
//        // create the fragments arr
//        fragments = new AdminReservationsFragment[tabTitles.length];
//
//        // create and set the pager adapter
//        pagerAdapter = new PagerAdapter(activity.getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);
//        tabLayout.setViewPager(viewPager);
//
//        // add page change listener to load the data only when the fragment is selected
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                // reverse the position cause the UI is arabic from right to left
//                position = tabTitles.length - 1 - position;
//
//                // check the fragment to load the data
//                AdminReservationsFragment fragment = fragments[position];
//                if (fragment != null) {
//                    fragment.loadDataIfRequired();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//
//        // this trick to pass the bug of adapter creation
//        // when select the tab its fragment is still not created so can't load the data
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // select last tab by default as the most right one
//                viewPager.setCurrentItem(tabTitles.length - 1);
//            }
//        }, 20);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // reverse the position cause the UI is arabic from right to left
            position = tabTitles.length - 1 - position;

            // get the fragment and check it
            AdminReservationsFragment fragment = fragments[position];
            if (fragment != null) {
                return fragment;
            }

            // prepare the reservations type to create the fragment
            ReservationsType reservationsType = null;
            switch (position) {
                case TODAY_RES_POS:
                    reservationsType = ReservationsType.ADMIN_TODAY_RESERVATIONS;
                    break;

                case ACCEPTED_RES_POS:
                    reservationsType = ReservationsType.ADMIN_ACCEPTED_RESERVATIONS;
                    break;

                case NEW_RES_POS:
                    reservationsType = ReservationsType.ADMIN_NEW_RESERVATIONS;
                    break;

                case PREVIOUS_RES_POS:
                    reservationsType = ReservationsType.ADMIN_PREVIOUS_RESERVATIONS;
                    break;

                case MY_RES_POS:
                    reservationsType = ReservationsType.ADMIN_MY_RESERVATIONS;
                    break;
            }

            // create the fragment and add it to the array
            fragment = new AdminReservationsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_RESERVATIONS_TYPE, reservationsType);
            fragment.setArguments(bundle);
            fragments[position] = fragment;

            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}

