package com.stadium.app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.dialogs.ChooseCityDialog;
import com.stadium.app.dialogs.ChooseFieldCapacityDialog;
import com.stadium.app.dialogs.ChooseFromAllDurationsDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.Duration;
import com.stadium.app.models.entities.FieldCapacity;
import com.stadium.app.models.entities.StadiumsFilter;
import com.stadium.app.utils.DatePickerFragment;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.Utils;

import java.util.Calendar;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class StadiumsSearchActivity extends ParentActivity {
    private StadiumsFilter filter;
    private View layoutContent;
    private Button btnCity;
    private EditText etName;
    private Button btnFieldCapacity;
    private Button btnDate;
    private Button btnTime;
    private Button btnSearch;

    private Rect contentLayoutRect; // to handle the outside click
    private ChooseCityDialog citiesDialog;
    private ChooseFieldCapacityDialog fieldsDialog;
    private DatePickerFragment datePickerFragment;
    private ChooseFromAllDurationsDialog timesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadiums_search);
        customizeLayoutParams();

        // customize the toolbar
        setToolbarIcon(R.drawable.white_close_icon);
        enableBackButton();

        // get the filter
        filter = (StadiumsFilter) getIntent().getSerializableExtra(Const.KEY_FILTER);

        // init views
        layoutContent = findViewById(R.id.layout_content);
        btnCity = (Button) findViewById(R.id.btn_city);
        etName = (EditText) findViewById(R.id.et_name);
        btnFieldCapacity = (Button) findViewById(R.id.btn_field_capacity);
        btnDate = (Button) findViewById(R.id.btn_date);
        btnTime = (Button) findViewById(R.id.btn_time);
        btnSearch = (Button) findViewById(R.id.btn_search);

        // add listeners
        btnCity.setOnClickListener(this);
        btnFieldCapacity.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        // create the content rect
        contentLayoutRect = new Rect();

        // check filter to update the ui or just create a new one
        if (filter != null) {
            updateUI();
        } else {
            filter = new StadiumsFilter();
        }
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

    private void updateUI() {
        updateCityUI();
        updateNameUI();
        updateFieldCapacityUI();
        updateDateUI();
        updateTimeUI();
    }

    private void updateCityUI() {
        if (filter.getCity() != null) {
            String str = getString(R.string.the_city) + ": " + filter.getCity().toString();
            btnCity.setText(str);
        } else {
            btnCity.setText(R.string.the_city);
        }
    }

    private void updateNameUI() {
        if (filter.getName() != null) {
            etName.setText(filter.getName());
        } else {
            etName.setText("");
        }
    }

    private void updateFieldCapacityUI() {
        if (filter.getFieldCapacity() != null) {
            String str = getString(R.string.field_capacity) + ": " + filter.getFieldCapacity();
            btnFieldCapacity.setText(str);
        } else {
            btnFieldCapacity.setText(R.string.field_capacity);
        }
    }

    private void updateDateUI() {
        if (filter.getDate() != null) {
            String date = DateUtils.formatDate(filter.getDate(), Const.SER_DATE_FORMAT, "d-M-yyyy");
            String str = getString(R.string.date) + ": " + date;
            btnDate.setText(str);
        } else {
            btnDate.setText(R.string.date);
        }
    }

    private void updateTimeUI() {
        if (filter.getTimeStart() != null && filter.getTimeEnd() != null) {
            String timeStart = DateUtils.formatDate(filter.getTimeStart(), Const.SER_TIME_FORMAT, "hh:mm a");
            String timeEnd = DateUtils.formatDate(filter.getTimeEnd(), Const.SER_TIME_FORMAT, "hh:mm a");
            String str = getString(R.string.time) + ": " + getString(R.string.from_x_to_x, timeStart, timeEnd);
            btnTime.setText(str);
        } else {
            btnTime.setText(R.string.time);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_city:
                chooseCity();
                break;

            case R.id.btn_field_capacity:
                chooseFieldCapacity();
                break;

            case R.id.btn_date:
                chooseDate();
                break;

            case R.id.btn_time:
                chooseTime();
                break;

            case R.id.btn_search:
                onSearch();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseCity() {
        if (citiesDialog == null) {
            citiesDialog = new ChooseCityDialog(this);
            citiesDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the city item
                    City city = (City) item;
                    if (city.getId() == 0) {
                        // all cities item
                        filter.setCity(null);
                    } else {
                        filter.setCity(city);
                    }
                    updateCityUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getCity() != null) {
            citiesDialog.setSelectedItemId(filter.getCity().getId());
        }

        citiesDialog.show();
    }

    private void chooseFieldCapacity() {
        if (fieldsDialog == null) {
            fieldsDialog = new ChooseFieldCapacityDialog(this);
            fieldsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the city item
                    FieldCapacity fieldCapacity = (FieldCapacity) item;
                    if (fieldCapacity.getName().equals(getString(R.string.all_sizes))) {
                        // all sizes item
                        filter.setFieldCapacity(null);
                    } else {
                        filter.setFieldCapacity(fieldCapacity.getName());
                    }
                    updateFieldCapacityUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getFieldCapacity() != null) {
            fieldsDialog.setSelectedItem(filter.getFieldCapacity());
        }

        fieldsDialog.show();
    }

    private void chooseDate() {
        // create the date picker fragment and customize it
        if (datePickerFragment == null) {
            datePickerFragment = new DatePickerFragment();

            // set max and min dates
            Calendar minDate = Calendar.getInstance();
            datePickerFragment.setMinDate(minDate);
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.DATE, Const.STADIUMS_SEARCH_MAX_DATE_DAYS_FROM_NOW);
            datePickerFragment.setMaxDate(maxDate);

            // add date set listener
            datePickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // set date and update its ui
                    String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    filter.setDate(date);
                    updateDateUI();
                }
            });
        }

        // show date dialog
        datePickerFragment.show(getSupportFragmentManager(), null);
    }

    private void chooseTime() {
        if (timesDialog == null) {
            timesDialog = new ChooseFromAllDurationsDialog(this);
            timesDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the city item
                    Duration duration = (Duration) item;
                    if (duration.getStartTime() == null && duration.getEndTime() == null
                            && getString(R.string.all_times).equals(duration.getDefaultName())) {
                        // all times item
                        filter.setTimeStart(null);
                        filter.setTimeEnd(null);
                    } else {
                        filter.setTimeStart(duration.getStartTime());
                        filter.setTimeEnd(duration.getEndTime());
                    }
                    updateTimeUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getTimeStart() != null && filter.getTimeEnd() != null) {
            timesDialog.setSelectedItem(filter.getTimeStart(), filter.getTimeEnd());
        }

        timesDialog.show();
    }

    private void onSearch() {
        String name = Utils.getText(etName);
        if (name.isEmpty()) {
            filter.setName(null);
        } else {
            filter.setName(name);
        }
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_FILTER, filter);
        setResult(RESULT_OK, intent);
        onBackPressed();
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
