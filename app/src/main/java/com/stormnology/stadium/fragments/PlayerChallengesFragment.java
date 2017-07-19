package com.stormnology.stadium.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.models.enums.ChallengesType;
import com.stormnology.stadium.views.SlidingTabLayout;

/*
 * Created by karam on 8/10/16.
 */
public class PlayerChallengesFragment extends ParentFragment {
    private static final int NEW_CHAL_POS = 0;
    private static final int ACCEPTED_CHAL_POS = 1;
    private static final int HISTORICAL_CHAL_POS = 2;
    private static final int MY_CHAL_POS = 3;

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private String[] tabTitles;
    private PagerAdapter pagerAdapter;
    private ChallengesFragment[] fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.the_challenges);
        createOptionsMenu(R.menu.menu_challenges);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_challenges, container, false);

        // init views
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // customize view pager & its tab layout
        tabTitles = getResources().getStringArray(R.array.player_challenges_tabs);
        tabLayout.setDistributeEvenly(true);
        updatePagerUI();

        return rootView;
    }

    private void updatePagerUI() {
        // create the fragments arr
        fragments = new ChallengesFragment[tabTitles.length];

        // create and set the pager adapter
        pagerAdapter = new PagerAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        // add page change listener to load the data only when the fragment is selected
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // reverse the position cause the UI is arabic from right to left
                position = tabTitles.length - 1 - position;

                // check the fragment to load the data
                ChallengesFragment fragment = fragments[position];
                if (fragment != null) {
                    fragment.loadDataIfRequired();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // this trick to pass the bug of adapter creation
        // when select the tab its fragment is still not created so can't load the data
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // select last tab by default as the most right one
                viewPager.setCurrentItem(tabTitles.length - 1);
            }
        }, 20);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
            ChallengesFragment fragment = fragments[position];
            if (fragment != null) {
                return fragment;
            }

            // prepare the challenges type to create the fragment
            ChallengesType challengesType = null;
            switch (position) {
                case NEW_CHAL_POS:
                    challengesType = ChallengesType.NEW_CHALLENGES;
                    break;

                case ACCEPTED_CHAL_POS:
                    challengesType = ChallengesType.ACCEPTED_CHALLENGES;
                    break;

                case HISTORICAL_CHAL_POS:
                    challengesType = ChallengesType.HISTORICAL_CHALLENGES;
                    break;

                case MY_CHAL_POS:
                    challengesType = ChallengesType.MY_CHALLENGES;
                    break;
            }

            // create the fragment and add it to the array
            fragment = new ChallengesFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_CHALLENGES_TYPE, challengesType);
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