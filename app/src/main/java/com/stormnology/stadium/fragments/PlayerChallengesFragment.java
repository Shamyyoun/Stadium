package com.stormnology.stadium.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.stormnology.stadium.activities.AddChallengeActivity;
import com.stormnology.stadium.activities.ChallengesSearchActivity;
import com.stormnology.stadium.models.entities.ChallengeInfoHolder;
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
    private ChallengeInfoHolder filter;

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
        // check item id
        if (item.getItemId() == R.id.action_search) {
            openSearchActivity();
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            openAddChallengeActivity();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openSearchActivity() {
        Intent intent = new Intent(activity, ChallengesSearchActivity.class);
        intent.putExtra(Const.KEY_FILTER, filter);
        startActivityForResult(intent, Const.REQ_SEARCH_CHALLENGES);
        activity.overridePendingTransition(R.anim.top_translate_enter, R.anim.no_anim);
    }

    private void openAddChallengeActivity() {
        Intent intent = new Intent(activity, AddChallengeActivity.class);
        startActivityForResult(intent, Const.REQ_ADD_CHALLENGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.REQ_SEARCH_CHALLENGES) {
                // update the  filter
                filter = (ChallengeInfoHolder) data.getSerializableExtra(Const.KEY_FILTER);
            } else if (requestCode == Const.REQ_ADD_CHALLENGE) {
                // refresh new challenges fragment
                refreshNewChallengesIfPossible();
            }
        }
    }

    private void refreshNewChallengesIfPossible() {
        // refresh it if not null and is the current screen
        ChallengesFragment fragment = fragments[NEW_CHAL_POS];
        int position = getReversedItemPosition(viewPager.getCurrentItem());
        if (fragment != null && position == NEW_CHAL_POS) {
            fragment.refresh();
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // reverse the position cause the UI is arabic from right to left
            position = getReversedItemPosition(position);

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

    private int getReversedItemPosition(int originalPosition) {
        return tabTitles.length - 1 - originalPosition;
    }
}