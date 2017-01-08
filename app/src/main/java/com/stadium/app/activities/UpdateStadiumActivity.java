package com.stadium.app.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.StadiumDurationsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.DurationController;
import com.stadium.app.controllers.StadiumController;
import com.stadium.app.interfaces.OnItemRemovedListener;
import com.stadium.app.models.entities.Duration;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.BitmapUtils;
import com.stadium.app.utils.DatePickerFragment;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by karam on 7/2/16.
 */
public class UpdateStadiumActivity extends PicPickerActivity {
    private static final String DISPLAYED_DATE_FORMAT = "yyyy/M/d";

    private Stadium stadium;
    private ActiveUserController userController;
    private StadiumController stadiumController;
    private DurationController durationController;

    private NestedScrollView scrollView;
    private ImageView ivImage;
    private EditText etTitle;
    private EditText etCity;
    private EditText etDesc;
    private Button btnStartDate;
    private View layoutDurations;
    private ImageButton ibAddDuration;
    private RecyclerView rvDurations;
    private Button btnUpdate;
    private Button btnCancel;

    private File image;
    private DatePickerFragment datePickerFragment;
    private List<Duration> durations;
    private StadiumDurationsAdapter durationsAdapter;
    private String startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_update_stadium);

        // obtain main objects
        stadium = (Stadium) getIntent().getSerializableExtra(Const.KEY_STADIUM);
        userController = new ActiveUserController(this);
        stadiumController = new StadiumController();
        durationController = new DurationController();

        // init views
        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etTitle = (EditText) findViewById(R.id.et_title);
        etCity = (EditText) findViewById(R.id.et_city);
        etDesc = (EditText) findViewById(R.id.et_desc);
        layoutDurations = findViewById(R.id.layout_durations);
        ibAddDuration = (ImageButton) findViewById(R.id.ib_add_duration);
        rvDurations = (RecyclerView) findViewById(R.id.rv_durations);
        btnStartDate = (Button) findViewById(R.id.btn_start_date);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // customize durations recycler
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvDurations.setLayoutManager(layoutManager);
        rvDurations.setNestedScrollingEnabled(false);

        // add listeners
        ivImage.setOnClickListener(this);
        btnStartDate.setOnClickListener(this);
        ibAddDuration.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        updateUI();
    }

    private void updateUI() {
        // set desc
        etDesc.setText(stadium.getBio());

        // set name
        String nameStr = getString(R.string.stadium) + ": " + stadium.getName();
        etTitle.setText(nameStr);

        // set city
        String city = stadiumController.getCityName(stadium);
        String cityStr = getString(R.string.city) + ": " + city;
        etCity.setText(cityStr);

        // load the image if possible
        Utils.loadImage(this, stadium.getImageLink(), R.drawable.default_image, ivImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                chooseImage();
                break;

            case R.id.btn_start_date:
                chooseDate();
                break;

            case R.id.ib_add_duration:
                onAddDuration();
                break;

            case R.id.btn_update:
                updateStadium();
                break;

            case R.id.btn_cancel:
                onBackPressed();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseImage() {
        // prepare the appropriate array
        String[] options = new String[]{
                getString(R.string.from_gallery),
                getString(R.string.from_camera)
        };

        // show list dialog
        DialogUtils.showListDialog(this, options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // switch the selected item
                switch (which) {
                    case 0:
                        setPickerAspects(Const.IMG_ASPECT_X_STADIUM, Const.IMG_ASPECT_Y_STADIUM);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_STADIUM);
                        pickFromGallery(0, true);
                        break;

                    case 1:
                        setPickerAspects(Const.IMG_ASPECT_X_STADIUM, Const.IMG_ASPECT_Y_STADIUM);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_STADIUM);
                        captureFromCamera(0, true);
                        break;
                }
            }
        });
    }

    @Override
    public void onImageReady(int requestCode, File image) {
        this.image = image;
        Picasso.with(this).load(image).placeholder(R.drawable.def_form_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(ivImage);
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
        durationsAdapter = new StadiumDurationsAdapter(this, durations, R.layout.item_stadium_duration);
        rvDurations.setAdapter(durationsAdapter);
        durationsAdapter.setOnItemRemovedListener(new OnItemRemovedListener() {
            @Override
            public void onItemRemoved(int position) {
                // show add btn
                ibAddDuration.setVisibility(View.VISIBLE);
            }
        });

        // show the layout
        layoutDurations.setVisibility(View.VISIBLE);
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
            ibAddDuration.setVisibility(View.GONE);
        }
    }

    private void addDuration() {
        // create and add the duration
        Duration duration = new Duration();
        duration.setDurationNumber(durations.size() + 1);
        durations.add(duration);
    }


    private void updateStadium() {
        hideKeyboard();

        // check start date
        if (startDate != null) {
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
        }

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // prepare params
        String desc = Utils.getText(etDesc);
        String encodedImage = null;
        String imageName = null;
        if (image != null) {
            // encode the image
            encodedImage = BitmapUtils.encodeBase64(image);

            // set the suitable image name
            if (stadium.getStadiumImage() != null) {
                imageName = stadium.getStadiumImage().getName();
            } else {
                imageName = "";
            }
        }

        // get the user
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.editStadium(this, this, user.getId(),
                user.getToken(), stadium.getId(), desc, encodedImage, imageName);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.API_STADIUM_PROFILE.equals(tag)) {
            // edit stadium request
            // check response
            Stadium stadium = (Stadium) response;
            if (statusCode == Const.SER_CODE_200 && stadium != null) {
                // set stadium obj & show msg
                this.stadium = stadium;
                Utils.showShortToast(this, R.string.stadium_updated_successfully);

                // invalidate the cached stadium image if changed
                if (image != null && stadium.getImageLink() != null) {
                    Picasso.with(this).invalidate(stadium.getImageLink());
                }

                // check start date
                if (startDate != null) {
                    // user changed durations,
                    // send the request
                    changeDurations();
                } else {
                    // didn't change durations,
                    // finish
                    onBackPressed();
                }
            } else {
                String errorMsg = AppUtils.getResponseMsg(this, response, R.string.failed_updating_stadium);
                Utils.showShortToast(this, errorMsg);
            }
        } else {
            // change durations request
            // check status code
            if (statusCode == Const.SER_CODE_200) {
                // show msg and finish
                Utils.showShortToast(this, R.string.durations_changed_successfully);
                onBackPressed();
            } else {
                String errorMsg = AppUtils.getResponseMsg(this, response, R.string.failed_changing_durations);
                Utils.showShortToast(this, errorMsg);
            }
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.API_STADIUM_PROFILE.equals(tag)) {
            // edit stadium request
            Utils.showShortToast(this, R.string.failed_updating_stadium);
        } else {
            // change durations request
            Utils.showShortToast(this, R.string.failed_changing_durations);
        }
    }

    private void changeDurations() {
        showProgressDialog();

        // get the user
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.changeDuration(this, this, user.getId(),
                user.getToken(), stadium.getId(), startDate, durations);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onBackPressed() {
        // set stadium as result and finish
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_STADIUM, stadium);
        setResult(RESULT_OK, intent);
        finish();
    }
}
