package com.stadium.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.StadiumController;
import com.stadium.app.dialogs.StadiumBioDialog;
import com.stadium.app.dialogs.StadiumContactsDialog;
import com.stadium.app.fragments.StadiumPeriodsFragment;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.Utils;
import com.stadium.app.views.SlidingTabLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/*
 * Created by karam on 7/31/16.
 */
public class StadiumInfoActivity extends ParentActivity {
    private int id;
    private StadiumController stadiumController;

    private ImageView ivImage;
    private TextView tvRating;
    private ImageButton ibContacts;
    private ImageButton ibLocation;
    private TextView tvName;
    private TextView tvDate;
    private ImageButton ibPreviousDay;
    private ImageButton ibNextDay;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private PagerAdapter pagerAdapter;
    private List<StadiumPeriodsFragment> fragments;
    private Stadium stadium;
    private List<String> fieldCapacities;

    private StadiumContactsDialog contactsDialog;
    private StadiumBioDialog bioDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_info);
        enableBackButton();

        // obtain main objects
        id = getIntent().getIntExtra(Const.KEY_ID, 0);
        stadiumController = new StadiumController();

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        ibContacts = (ImageButton) findViewById(R.id.ib_contacts);
        ibLocation = (ImageButton) findViewById(R.id.ib_location);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        ibPreviousDay = (ImageButton) findViewById(R.id.ib_previous_day);
        ibNextDay = (ImageButton) findViewById(R.id.ib_next_day);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // add listeners
        ibContacts.setOnClickListener(this);
        ibLocation.setOnClickListener(this);
        tvName.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        ibPreviousDay.setOnClickListener(this);
        ibNextDay.setOnClickListener(this);

        // update date ui with current date
        updateDateUI(null);

        // load stadium info
        loadStadiumInfo();
    }

    private void updateDateUI(String date) {
        if (date == null) {
            // get current date
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            date = DateUtils.convertToString(calendar, "yyyy/MM/dd");
        }

        tvDate.setText(date);
    }

    private void updateStadiumUI() {
        // set basic info
        tvName.setText(stadium.getName());
        tvRating.setText(Utils.formatDouble(stadium.getRate()));

        // load the image
        Utils.loadImage(this, stadium.getImageLink(), R.drawable.default_image, ivImage);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_contacts:
                onContacts();
                break;

            case R.id.ib_location:
                onLocation();
                break;

            case R.id.tv_name:
                onName();
                break;

            default:
                super.onClick(v);
        }
    }

    private void onContacts() {
        // check if this stadium has contacts info
        if (stadiumController.hasContactInfo(stadium)) {
            // open contacts dialog
            if (contactsDialog == null) {
                contactsDialog = new StadiumContactsDialog(this, stadium);
            }
            contactsDialog.show();
        } else {
            Utils.showShortToast(this, R.string.no_contacts_info_available);
        }
    }

    private void onLocation() {
        // check if this stadium has location
        if (stadiumController.hasLocation(stadium)) {
            Utils.openMapIntent(this, stadium.getName(), stadium.getLatitude(), stadium.getLongitude());
        } else {
            Utils.showShortToast(this, R.string.no_location_info_available);
        }
    }

    private void onName() {
        // check if this stadium has bio
        if (!Utils.isNullOrEmpty(stadium.getBio())) {
            // open bio dialog
            if (bioDialog == null) {
                bioDialog = new StadiumBioDialog(this, stadium);
            }
            bioDialog.show();
        } else {
            Utils.showShortToast(this, R.string.no_bio_available);
        }
    }

    private void loadStadiumInfo() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            enableControls(false);
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getStadiumInfo(this, this, id);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.API_GET_STADIUM_INFO.equals(tag)) {
            Stadium stadium = (Stadium) response;

            // check result
            if (statusCode == Const.SER_CODE_200 && stadium != null) {
                this.stadium = stadium;
                updateStadiumUI();

//                // load data in fragments
//                updatePagerUI();
//                playersFragment.loadData();
//                reservationsFragment.loadData();

                // enable the controls
                enableControls(true);
            } else {
                // get and show error msg
                String errorMsg = AppUtils.getResponseError(this, stadium);
                if (errorMsg == null) {
                    errorMsg = getString(R.string.failed_loading_info);
                }
                Utils.showShortToast(this, errorMsg);

                // disable the controls
                enableControls(false);
            }
        } else {
            super.onSuccess(response, statusCode, tag);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        if (Const.API_GET_TEAM_INFO.equals(tag)) {
            super.onFail(ex, statusCode, tag);
            // disable the controls
            enableControls(false);
        } else {
            super.onFail(ex, statusCode, tag);
        }
    }

    private void enableControls(boolean enable) {
        ibContacts.setEnabled(enable);
        ibLocation.setEnabled(enable);
        tvDate.setEnabled(enable);
        ibPreviousDay.setEnabled(enable);
        ibNextDay.setEnabled(enable);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // get fragment and check it
            StadiumPeriodsFragment fragment = fragments.get(position);
            if (fragment == null) {
                // create new one and pass its arguments
                fragment = new StadiumPeriodsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Const.KEY_STADIUM, stadium);
                fragment.setArguments(bundle);
            }

            // return it
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fieldCapacities.get(position);
        }
    }
}