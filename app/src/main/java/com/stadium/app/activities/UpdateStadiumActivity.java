package com.stadium.app.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.adapters.StadiumDurationsAdapter;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.StadiumController;
import com.stadium.app.models.SerializableListWrapper;
import com.stadium.app.models.entities.Duration;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.responses.DurationsResponse;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.BitmapUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by karam on 7/2/16.
 */
public class UpdateStadiumActivity extends PicPickerActivity {
    private Stadium stadium;
    private ActiveUserController userController;
    private StadiumController stadiumController;

    private ImageView ivImage;
    private EditText etTitle;
    private EditText etCity;
    private EditText etDesc;
    private ProgressBar pbDurations;
    private View layoutCurrentDurations;
    private RecyclerView rvCurrentDurations;
    private View layoutNextDurations;
    private RecyclerView rvNextDurations;
    private TextView tvAddNextDurations;
    private Button btnUpdate;
    private Button btnCancel;

    private File image;
    private List<Duration> currentDurations;
    private List<Duration> nextDurations;
    private StadiumDurationsAdapter currentDurationsAdapter;
    private StadiumDurationsAdapter nextDurationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_update_stadium);

        // obtain main objects
        stadium = (Stadium) getIntent().getSerializableExtra(Const.KEY_STADIUM);
        userController = new ActiveUserController(this);
        stadiumController = new StadiumController();

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etTitle = (EditText) findViewById(R.id.et_title);
        etCity = (EditText) findViewById(R.id.et_city);
        etDesc = (EditText) findViewById(R.id.et_desc);
        pbDurations = (ProgressBar) findViewById(R.id.pb_durations);
        layoutCurrentDurations = findViewById(R.id.layout_current_durations);
        rvCurrentDurations = (RecyclerView) findViewById(R.id.rv_current_durations);
        layoutNextDurations = findViewById(R.id.layout_next_durations);
        rvNextDurations = (RecyclerView) findViewById(R.id.rv_next_durations);
        tvAddNextDurations = (TextView) findViewById(R.id.tv_add_next_durations);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        ivImage.setOnClickListener(this);
        tvAddNextDurations.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        updateUI();
        loadDurations();
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

    private void updateCurrentDurationsUI() {
        // customize the recycler view
        customizeRecyclerView(rvCurrentDurations);

        // create and set the durations adapter
        currentDurationsAdapter = new StadiumDurationsAdapter(this, currentDurations, R.layout.item_stadium_duration);
        rvCurrentDurations.setAdapter(currentDurationsAdapter);

        // show the layout
        layoutCurrentDurations.setVisibility(View.VISIBLE);
    }

    private void updateNextDurationsUI() {
        // customize the recycler view
        customizeRecyclerView(rvNextDurations);

        // create and set the durations adapter
        nextDurationsAdapter = new StadiumDurationsAdapter(this, nextDurations, R.layout.item_stadium_duration);
        rvNextDurations.setAdapter(nextDurationsAdapter);

        // show the layout
        layoutNextDurations.setVisibility(View.VISIBLE);
    }

    private void customizeRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                chooseImage();
                break;

            case R.id.tv_add_next_durations:
                openAddDurationsActivity();
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

    private void openAddDurationsActivity() {
        Intent intent = new Intent(this, AddDurationsActivity.class);
        startActivityForResult(intent, Const.REQ_ADD_DURATIONS);
        overridePendingTransition(R.anim.top_translate_enter, R.anim.no_anim);
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

    private void loadDurations() {
        // check the internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showDurationsProgress(true);

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.getMyDurations(this, this, stadium.getId());
        cancelWhenDestroyed(connectionHandler);
    }


    private void updateStadium() {
        hideKeyboard();

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
        // hide progress
        hideProgressDialog();
        showDurationsProgress(false);

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

                // finish the activity
                finishActivity();
            } else {
                String errorMsg = AppUtils.getResponseMsg(this, response, R.string.failed_updating_stadium);
                Utils.showShortToast(this, errorMsg);
            }
        } else if (Const.API_GET_MY_DURATIONS.equals(tag)) {
            // my durations request
            // check response
            DurationsResponse durationsResponse = (DurationsResponse) response;
            if (durationsResponse != null) {
                // check current durations
                if (!Utils.isNullOrEmpty(durationsResponse.getTimes())) {
                    // update it ui
                    currentDurations = durationsResponse.getTimes();
                    updateCurrentDurationsUI();
                }

                // check next durations
                if (!Utils.isNullOrEmpty(durationsResponse.getNextTimes())) {
                    // update it ui
                    nextDurations = durationsResponse.getNextTimes();
                    updateNextDurationsUI();
                } else {
                    // show add durations tv
                    showAddDurations(true);
                }
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, response, R.string.failed_loading_durations);
                Utils.showShortToast(this, msg);
            }
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        super.onFail(ex, statusCode, tag);
        showDurationsProgress(false);
    }

    private void showDurationsProgress(boolean show) {
        pbDurations.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showAddDurations(boolean show) {
        tvAddNextDurations.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_ADD_DURATIONS && resultCode == RESULT_OK) {
            // update next durations
            SerializableListWrapper<Duration> durationsWrapper = (SerializableListWrapper<Duration>) data.getSerializableExtra(Const.KEY_DURATIONS);
            nextDurations = durationsWrapper.getList();
            updateNextDurationsUI();

            // hide add durations
            showAddDurations(false);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void finishActivity() {
        // set result and finish
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_STADIUM, stadium);
        setResult(RESULT_OK, intent);
        finish();
    }
}
