package com.stadium.player.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.stadium.player.R;
import com.stadium.player.fragments.AverageIntensityFragment;
import com.stadium.player.fragments.BigIntensityFragment;
import com.stadium.player.fragments.SmallIntensityFragment;
import com.stadium.player.fragments.TeamPlayersFragment;
import com.stadium.player.fragments.TeamReservationsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class StadiumOrderActivity extends ParentToolbarActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_stadium);
        setTitle("");
        setToolbarIcon(R.drawable.search_icon);

        // init views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BigIntensityFragment(), "كبيرة (٢٠٠ ريال)");
        adapter.addFragment(new AverageIntensityFragment(), "متوسطة (١٠٠ ريال)");
        adapter.addFragment(new SmallIntensityFragment(), "صغيرة (١٠٠ ريال)");

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}