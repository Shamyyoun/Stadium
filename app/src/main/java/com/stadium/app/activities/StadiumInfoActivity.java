package com.stadium.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.dialogs.ContactDialog;
import com.stadium.app.dialogs.StadiumInfoDialog;
import com.stadium.app.fragments.AverageIntensityFragment;
import com.stadium.app.fragments.BigIntensityFragment;
import com.stadium.app.fragments.SmallIntensityFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * Created by karam on 7/31/16.
 */
public class StadiumInfoActivity extends ParentActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TextView date;
    private TextView stadiumName;

    private ImageButton stadiumContact;
    private ImageButton stadiumLocation;

    private ImageButton leftArrow;
    private ImageButton rightArrow;

    private int mYear, mMonth, mDay;
    private long minDate;
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_info);
        setTitle("");
        setToolbarIcon(R.drawable.search_icon);

        // init views
        date = (TextView) findViewById(R.id.tv_date);
        date.setOnClickListener(this);
        stadiumName = (TextView) findViewById(R.id.tv_stadium_name);
        stadiumName.setOnClickListener(this);

        stadiumContact = (ImageButton) findViewById(R.id.ib_contact);
        stadiumContact.setOnClickListener(this);
        stadiumLocation = (ImageButton) findViewById(R.id.ib_location);
        stadiumLocation.setOnClickListener(this);

        leftArrow = (ImageButton) findViewById(R.id.iv_left_arrow);
        leftArrow.setOnClickListener(this);
        rightArrow = (ImageButton) findViewById(R.id.iv_right_arrow);
        rightArrow.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Get Current Date
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        date.setText(mDay + "/" + mMonth + "/" + mYear + "");

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BigIntensityFragment(), "كبيرة (٢٠٠ ريال)");
        adapter.addFragment(new AverageIntensityFragment(), "متوسطة (١٠٠ ريال)");
        adapter.addFragment(new SmallIntensityFragment(), "صغيرة (١٠٠ ريال)");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.tv_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mDay, mYear, mMonth);

                minDate = c.getTime().getTime(); // Twice!
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.show();
                break;

            case R.id.iv_left_arrow:

                break;

            case R.id.iv_right_arrow:
                break;

            case R.id.ib_contact:

                ContactDialog contactDialog = new ContactDialog(this);
                contactDialog.show();
                break;

            case R.id.tv_stadium_name:

                StadiumInfoDialog stadiumInfoDialog = new StadiumInfoDialog(this);
                stadiumInfoDialog.show();
                break;
            case R.id.ib_location:

                break;

        }
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