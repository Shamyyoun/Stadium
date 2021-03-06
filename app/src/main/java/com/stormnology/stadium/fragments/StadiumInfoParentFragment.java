package com.stormnology.stadium.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.activities.ViewImageActivity;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.StadiumController;
import com.stormnology.stadium.dialogs.StadiumContactsDialog;
import com.stormnology.stadium.models.entities.Field;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DatePickerFragment;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.Utils;
import com.stormnology.stadium.views.SlidingTabLayout;

import java.util.Calendar;
import java.util.Locale;

/*
 * Created by karam on 8/10/16.
 */
public abstract class StadiumInfoParentFragment extends ParentFragment {
    private static final String DISPLAYED_DATE_FORMAT = "EEEE yyyy/M/d";

    private Team selectedTeam; // this is the team object when the user navigates to the add players from team info screen
    protected int id;
    private boolean isAdminStadiumScreen;
    private Reservation dataHolder;
    protected StadiumController stadiumController;

    private ImageView ivImage;
    private TextView tvRating;
    protected TextView tvName;
    private TextView tvDescription;
    private TextView tvDate;
    private ImageButton ibPreviousDay;
    private ImageButton ibNextDay;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private PagerAdapter pagerAdapter;
    private StadiumPeriodsFragment[] fragments;
    protected Stadium stadium;
    private Field[] fieldCapacities;
    private boolean controlsEnabled;
    private DatePickerFragment datePickerFragment;
    private StadiumContactsDialog contactsDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // obtain main objects
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(Const.KEY_ID, 0);
            selectedTeam = (Team) bundle.getSerializable(Const.KEY_TEAM);
        }
        dataHolder = new Reservation();
        stadiumController = new StadiumController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewResId(), container, false);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDescription = (TextView) findViewById(R.id.tv_desc);
        tvDate = (TextView) findViewById(R.id.tv_date);
        ibPreviousDay = (ImageButton) findViewById(R.id.ib_previous_day);
        ibNextDay = (ImageButton) findViewById(R.id.ib_next_day);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        // add listeners
        ivImage.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        ibPreviousDay.setOnClickListener(this);
        ibNextDay.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // update date ui with current date
        updateDateUI();

        // load stadium info
        loadStadiumInfo();
    }

    /**
     * used to get the fragment layout id from children
     *
     * @return
     */
    protected abstract int getContentViewResId();

    private void updateDateUI() {
        // check the date in data holder
        if (dataHolder.getDate() == null) {
            // get current date and set it in data hodler
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            String date = DateUtils.convertToString(calendar, Const.SER_DATE_FORMAT);
            dataHolder.setDate(date);
        }

        // format the date and set it in the text view
        String formattedDate = DateUtils.formatDate(dataHolder.getDate(),
                Const.SER_DATE_FORMAT, DISPLAYED_DATE_FORMAT, Utils.getArabicLocale());
        tvDate.setText(formattedDate);
    }

    protected void updateStadiumUI() {
        // set basic info
        tvName.setText(stadium.getName());
        tvRating.setText(Utils.formatDouble(stadium.getRate()));

        // set description
        String desc = stadium.getBio();
        if (!Utils.isNullOrEmpty(desc)) {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(desc);
        } else {
            tvDescription.setVisibility(View.GONE);
        }

        // load the image
        Utils.loadImage(activity, stadium.getImageLink(), R.drawable.default_image, ivImage);
    }

    private void updatePagerUI() {
        // create the adapter
        fragments = new StadiumPeriodsFragment[fieldCapacities.length];
        pagerAdapter = new PagerAdapter(activity.getSupportFragmentManager());

        // set the adapter and tab layout
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setTextSize((int) getResources().getDimension(R.dimen.small_text));
        tabLayout.setViewPager(viewPager);

        // set off screen page limit to fragments count if 3 or lower
        if (fragments.length <= 3) {
            viewPager.setOffscreenPageLimit(fragments.length);
        }

        // select last tab by default as the most right one
        viewPager.setCurrentItem(fieldCapacities.length - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                openViewImageActivity();
                break;

            case R.id.tv_date:
                chooseDate();
                break;

            case R.id.ib_previous_day:
                decreaseDate();
                break;

            case R.id.ib_next_day:
                increaseDate();
                break;

            default:
                super.onClick(v);
        }
    }

    private void openViewImageActivity() {
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra(Const.KEY_IMAGE_URL, stadium.getImageLink());
        startActivity(intent);
        activity.overridePendingTransition(R.anim.scale_fade_enter, R.anim.scale_fade_exit);
    }

    protected void onContacts() {
        // check if this stadium has contacts info
        if (stadiumController.hasContactInfo(stadium)) {
            // open contacts dialog
            if (contactsDialog == null) {
                contactsDialog = new StadiumContactsDialog(activity, stadium);
            }
            contactsDialog.show();
        } else {
            Utils.showShortToast(activity, R.string.no_contacts_info_available);
        }
    }

    protected void onLocation() {
        // check if this stadium has location
        if (stadiumController.hasLocation(stadium)) {
            Utils.openMapIntent(activity, stadium.getName(), stadium.getLatitude(), stadium.getLongitude());
        } else {
            Utils.showShortToast(activity, R.string.no_location_info_available);
        }
    }

    private void chooseDate() {
        // create the date picker fragment and customize it
        if (datePickerFragment == null) {
            datePickerFragment = new DatePickerFragment();

            // set min date
            Calendar midDate = Calendar.getInstance();
            datePickerFragment.setMinDate(midDate);

            // add date set listener
            datePickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // set date in data holder and update its ui
                    String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    date = DateUtils.formatDate(date, "yyyy/M/d", Const.SER_DATE_FORMAT);
                    dataHolder.setDate(date);
                    updateDateUI();

                    // refresh the fragments
                    refreshPeriods();
                }
            });
        }

        // set selected date
        Calendar date = DateUtils.convertToCalendar(dataHolder.getDate(), Const.SER_DATE_FORMAT);
        datePickerFragment.setDate(date);

        // show date dialog
        datePickerFragment.show(activity.getSupportFragmentManager(), null);
    }

    private void decreaseDate() {
        // get current date calendar
        Calendar date = DateUtils.convertToCalendar(dataHolder.getDate(), Const.SER_DATE_FORMAT);

        // check this date
        if (date != null && !DateUtils.isToday(date)) {
            // this is a day higher than today
            // decrease it and set it in the data holder then update its ui
            date.add(Calendar.DATE, -1);
            String dateStr = DateUtils.convertToString(date, Const.SER_DATE_FORMAT);
            dataHolder.setDate(dateStr);
            updateDateUI();

            // refresh the fragments
            refreshPeriods();
        }
    }

    private void increaseDate() {
        // get the new date after increasing it one day
        String date = DateUtils.getNewStringDate(dataHolder.getDate(), Const.SER_DATE_FORMAT, 1);
        if (date != null) {
            // set the new date in data holder and update its ui
            dataHolder.setDate(date);
            updateDateUI();

            // then refresh the fragments
            refreshPeriods();
        }
    }

    private void refreshPeriods() {
        // check fragments first
        if (fragments == null) {
            return;
        }

        // loop for fragments to update each one
        for (StadiumPeriodsFragment fragment : fragments) {
            if (fragment != null) {
                // update the date and refresh it
                fragment.updateDate(dataHolder.getDate());
                fragment.refresh();
            }
        }
    }

    private void loadStadiumInfo() {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            enableControls(false);
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getStadiumInfo(activity, this, id);
        cancelWhenDestroyed(connectionHandler);
    }

    private void loadFieldCapacities() {
        // send request
        ConnectionHandler connectionHandler = ApiRequests.fieldSizes(activity, this, stadium.getId());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        // check tag
        if (Const.API_GET_STADIUM_INFO.equals(tag)) {
            Stadium stadium = (Stadium) response;

            // check result
            if (statusCode == Const.SER_CODE_200 && stadium != null) {
                this.stadium = stadium;
                updateStadiumUI();
                loadFieldCapacities();

                // enable the controls
                enableControls(true);
            } else {
                // hide progress
                hideProgressDialog();

                // get and show error msg
                String errorMsg = AppUtils.getResponseMsg(activity, stadium, R.string.failed_loading_info);
                Utils.showShortToast(activity, errorMsg);

                // disable the controls
                enableControls(false);
            }
        } else if (Const.API_FIELD_SIZE.equals(tag)) {
            // hide progress
            hideProgressDialog();

            // get field capacities and reverse it
            fieldCapacities = (Field[]) response;
            fieldCapacities = (Field[]) Utils.reverseArray(fieldCapacities);

            // enable the date controls
            enableDateControls(true);

            // update the pager ui
            updatePagerUI();
        } else {
            super.onSuccess(response, statusCode, tag);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();

        if (Const.API_GET_STADIUM_INFO.equals(tag)) {
            Utils.showShortToast(activity, R.string.failed_loading_info);
            enableControls(false);
        } else if (Const.API_FIELD_SIZE.equals(tag)) {
            Utils.showShortToast(activity, R.string.failed_loading_field_sizes);
            enableDateControls(false);
        } else {
            super.onFail(ex, statusCode, tag);
        }
    }

    protected void enableControls(boolean enable) {
        ivImage.setEnabled(enable);
        tvDate.setEnabled(enable);
        ibPreviousDay.setEnabled(enable);
        ibNextDay.setEnabled(enable);
        controlsEnabled = enable;
    }

    private void enableDateControls(boolean enable) {
        tvDate.setEnabled(enable);
        ibPreviousDay.setEnabled(enable);
        ibNextDay.setEnabled(enable);
    }

    public void setAdminStadiumScreen(boolean isAdminStadiumFragment) {
        this.isAdminStadiumScreen = isAdminStadiumFragment;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // get fragment and check it
            StadiumPeriodsFragment fragment = fragments[position];
            if (fragment == null) {
                // prepare reservation object with all required parameters
                Reservation reservation = new Reservation();
                reservation.setReservationStadium(stadium);
                reservation.setDate(dataHolder.getDate());
                reservation.setField(fieldCapacities[position]);

                // create bundle with this object
                Bundle bundle = new Bundle();
                bundle.putSerializable(Const.KEY_RESERVATION, reservation);
                bundle.putSerializable(Const.KEY_TEAM, selectedTeam);
                bundle.putSerializable(Const.KEY_IS_ADMIN_STADIUM_SCREEN, isAdminStadiumScreen);

                // create new fragment and pass its arguments
                fragment = new StadiumPeriodsFragment();
                fragment.setArguments(bundle);

                // add this new item to the fragments arr
                fragments[position] = fragment;
            }

            // return it
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Field field = fieldCapacities[position];
            String pageTitle = field.getFieldSize() + " (" + field.getPrice() + " " + getString(R.string.currency) + ")";
            return pageTitle;
        }
    }

    protected boolean isControlsEnabled() {
        return controlsEnabled;
    }
}

