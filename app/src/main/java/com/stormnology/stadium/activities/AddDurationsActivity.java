package com.stormnology.stadium.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.StadiumDurationsAdapter;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.DurationController;
import com.stormnology.stadium.interfaces.OnItemRemovedListener;
import com.stormnology.stadium.models.SerializableListWrapper;
import com.stormnology.stadium.models.entities.Duration;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.DatePickerFragment;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class AddDurationsActivity extends ParentActivity {
    private static final String DISPLAYED_DATE_FORMAT = "yyyy/M/d";

    private ActiveUserController activeUserController;
    private DurationController durationController;

    private View layoutContent;
    private NestedScrollView scrollView;
    private Button btnStartDate;
    private RecyclerView rvDurations;
    private TextView tvAddDuration;
    private Button btnAdd;

    private Rect contentLayoutRect; // to handle the outside click
    private DatePickerFragment datePickerFragment;
    private List<Duration> durations;
    private StadiumDurationsAdapter durationsAdapter;
    private String startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_durations);
        customizeLayoutParams();

        // customize the toolbar
        setToolbarIcon(R.drawable.white_close_icon);
        enableBackButton();

        activeUserController = new ActiveUserController(this);
        durationController = new DurationController();

        // init views
        layoutContent = findViewById(R.id.layout_content);
        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        btnStartDate = (Button) findViewById(R.id.btn_start_date);
        rvDurations = (RecyclerView) findViewById(R.id.rv_durations);
        tvAddDuration = (TextView) findViewById(R.id.tv_add_duration);
        btnAdd = (Button) findViewById(R.id.btn_add);

        // customize durations recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvDurations.setLayoutManager(layoutManager);

        // add listeners
        btnStartDate.setOnClickListener(this);
        tvAddDuration.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        // create the content rect
        contentLayoutRect = new Rect();
    }

    private void customizeLayoutParams() {
        // customize the layout params:
        // set the activity width and height to match parent and its gravity to the top
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_date:
                chooseDate();
                break;

            case R.id.tv_add_duration:
                onAddDuration();
                break;

            case R.id.btn_add:
                changeDurations();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseDate() {
        // create the date picker fragment and customize it
        if (datePickerFragment == null) {
            datePickerFragment = new DatePickerFragment();

            // set min date
            Calendar minDate = DateUtils.addDays(Const.UPDATE_STADIUM_MIN_DATE_DAYS_FROM_NOW);
            datePickerFragment.setMinDate(minDate);

            // add date set listener
            datePickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // update the ui
                    String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    btnStartDate.setText(date);

                    // check start date
                    if (startDate == null) {
                        // update durations ui
                        updateDurationsUI();
                    }

                    // set the date
                    startDate = DateUtils.formatDate(date, DISPLAYED_DATE_FORMAT, Const.SER_DATE_FORMAT);
                }
            });
        }

        // show date dialog
        datePickerFragment.show(getSupportFragmentManager(), null);
    }

    private void updateDurationsUI() {
        // create durations list
        durations = new ArrayList<>();

        // add first item
        addDuration();

        // create and set the durations adapter
        durationsAdapter = new StadiumDurationsAdapter(this, durations, R.layout.item_update_stadum_duration);
        rvDurations.setAdapter(durationsAdapter);
        durationsAdapter.setOnItemRemovedListener(new OnItemRemovedListener() {
            @Override
            public void onItemRemoved(int position) {
                // show add tv
                tvAddDuration.setVisibility(View.VISIBLE);
            }
        });

        // show add tv
        tvAddDuration.setVisibility(View.VISIBLE);
    }

    private void onAddDuration() {
        // add to the list and notify the adapter
        addDuration();
        durationsAdapter.notifyDataSetChanged();

        // scroll scrollview to bottom
        scrollView.fullScroll(View.FOCUS_DOWN);

        // check new size
        if (durations.size() == Const.UPDATE_STADIUM_MAX_DURATIONS_COUNT) {
            // hide add btn
            tvAddDuration.setVisibility(View.GONE);
        }
    }

    private void addDuration() {
        // create and add the duration
        Duration duration = new Duration();
        duration.setDurationNumber(durations.size() + 1);
        durations.add(duration);
    }

    private void changeDurations() {
        // check start date
        if (startDate == null) {
            Utils.showShortToast(this, R.string.choose_start_date);
            return;
        }

        // ensure that user has filled all durations
        if (!durationController.checkDurationsFilled(durations)) {
            Utils.showShortToast(this, R.string.please_fill_all_times);
            return;
        }

        // ensure that no nested durations
        if (!durationController.checkNoNestedDurations(durations)) {
            Utils.showShortToast(this, R.string.nested_durations_found);
            return;
        }

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // send request
        User user = activeUserController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.changeDuration(this, this, user.getId(),
                user.getToken(), user.getAdminStadium().getId(), startDate, durations);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check status code
        if (statusCode == Const.SER_CODE_200) {
            // show msg
            Utils.showShortToast(this, R.string.durations_changed_successfully);

            // set result
            Intent intent = new Intent();
            SerializableListWrapper<Duration> durationsWrapper = new SerializableListWrapper<>(durations);
            intent.putExtra(Const.KEY_DURATIONS, durationsWrapper);
            setResult(RESULT_OK, intent);

            // finish
            onBackPressed();
        } else {
            String errorMsg = AppUtils.getResponseMsg(this, response, R.string.failed_changing_durations);
            Utils.showShortToast(this, errorMsg);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.top_translate_exit);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // get x, y
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();

        // check if this point is outside the content layout
        layoutContent.getDrawingRect(contentLayoutRect);
        if (ev.getAction() == MotionEvent.ACTION_UP
                && !contentLayoutRect.contains(x, y)) {
            onBackPressed();
        }

        return super.dispatchTouchEvent(ev);
    }
}
