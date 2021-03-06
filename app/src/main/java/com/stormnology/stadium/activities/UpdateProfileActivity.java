package com.stormnology.stadium.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.CityController;
import com.stormnology.stadium.dialogs.ChoosePositionDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.City;
import com.stormnology.stadium.models.entities.Position;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.DatePickerFragment;
import com.stormnology.stadium.utils.DateUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 6/29/16.
 */
public class UpdateProfileActivity extends ParentActivity {
    private static final String DISPLAYED_DATE_FORMAT = "yyyy/M/d";
    private ActiveUserController activeUserController;
    private ImageView ivImage;
    private EditText etName;
    private Button btnBirthdate;
    private Spinner spCity;
    private EditText etPhone;
    private Button btnPosition;
    private EditText etEmail;
    private EditText etBio;
    private Button btnUpdate;

    private List<City> cities;
    private DatePickerFragment datePickerFragment;
    private ChoosePositionDialog positionsDialog;
    private Position selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // create the user controller
        activeUserController = new ActiveUserController(this);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etName = (EditText) findViewById(R.id.et_name);
        btnBirthdate = (Button) findViewById(R.id.btn_birthdate);
        spCity = (Spinner) findViewById(R.id.sp_city);
        etPhone = (EditText) findViewById(R.id.et_phone);
        btnPosition = (Button) findViewById(R.id.btn_position);
        etEmail = (EditText) findViewById(R.id.et_email);
        etBio = (EditText) findViewById(R.id.et_bio);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        // add listeners
        btnBirthdate.setOnClickListener(this);
        btnPosition.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        // update and load cities
        updateCitiesUI();
        loadCities();

        // update ui from saved user
        updateUI();

        etName.requestFocus();
    }

    private void updateUI() {
        User user = activeUserController.getUser();

        // set basic info
        etName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etEmail.setText(Utils.trim(user.getEmail()));
        etBio.setText(user.getBio());

        // set birthdate
        String birthdate = DateUtils.formatDate(user.getDateOfBirth(), Const.SER_DATE_FORMAT, DISPLAYED_DATE_FORMAT);
        btnBirthdate.setText(Utils.isNullOrEmpty(birthdate) ? getString(R.string.birthdate) : birthdate);

        // set the position
        String position = Utils.isNullOrEmpty(user.getPosition()) ? getString(R.string.position) : user.getPosition();
        btnPosition.setText(position);

        // set the city
        City city = user.getCity();
        if (city != null) {
            CityController cityController = new CityController();
            if (cities != null) {
                int itemPosition = cityController.getItemPosition(cities, city.getId());
                if (itemPosition != -1) {
                    spCity.setSelection(itemPosition);
                }
            } else {
                cities = new ArrayList<>();
                cities.add(city);
                cities = cityController.addDefaultItem(cities, getString(R.string.select_city));
                spCity.setSelection(1);
            }
        }

        // load the image
        Utils.loadImage(this, user.getImageLink(), R.drawable.default_image, ivImage);
    }

    private void loadCities() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.failed_loading_cities);
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.getCities(this, this);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_birthdate:
                chooseBirthdate();
                break;

            case R.id.btn_position:
                choosePosition();
                break;

            case R.id.btn_update:
                update();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseBirthdate() {
        // create the date picker fragment if required
        if (datePickerFragment == null) {
            datePickerFragment = new DatePickerFragment();

            // set dates
            datePickerFragment.setMinDate(Const.USER_MIN_BIRTHDATE, Const.SER_DATE_FORMAT);
            datePickerFragment.setMaxDate(Const.USER_MAX_BIRTHDATE, Const.SER_DATE_FORMAT);
            datePickerFragment.setDate(activeUserController.getUser().getDateOfBirth(), Const.SER_DATE_FORMAT);

            // add date set listener
            datePickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // update the ui
                    String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    btnBirthdate.setText(date);
                }
            });
        }

        // show date dialog
        datePickerFragment.show(getSupportFragmentManager(), null);
    }

    private void choosePosition() {
        if (positionsDialog == null) {
            positionsDialog = new ChoosePositionDialog(this);
            positionsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // set position and update its ui
                    selectedPosition = (Position) item;
                    btnPosition.setText(selectedPosition.getName());
                }
            });
        }

        // check to select item if possible
        if (selectedPosition != null) {
            positionsDialog.setSelectedItem(selectedPosition.getName());
        } else if (activeUserController.getUser().getPosition() != null) {
            positionsDialog.setSelectedItem(activeUserController.getUser().getPosition());
        }

        positionsDialog.show();
    }

    private void update() {
        // get the user
        User user = activeUserController.getUser();

        // prepare params
        String birthdate = DateUtils.formatDate(btnBirthdate.getText().toString(), DISPLAYED_DATE_FORMAT, Const.SER_DATE_FORMAT);
        City city = (City) spCity.getSelectedItem();
        String phone = Utils.getText(etPhone);
        String position = this.selectedPosition != null ? this.selectedPosition.getName() : user.getPosition();
        String email = Utils.getText(etEmail);
        String bio = Utils.getText(etBio);

        // validate inputs
        if (birthdate == null) {
            Utils.showShortToast(this, R.string.choose_birthdate);
            return;
        }
        if (city.getId() == 0) {
            Utils.showShortToast(this, R.string.please_select_city);
            return;
        }
        if (Utils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.required));
            return;
        }
        if (!Utils.isEmpty(email) && !Utils.isValidEmail(email)) {
            etEmail.setError(getString(R.string.invalid_email_format));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.editProfile(this, this, user.getId(),
                user.getToken(), birthdate, city, phone, position, email, bio);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        switch (tag) {
            case Const.API_GET_CITIES:
                if (statusCode == Const.SER_CODE_200) {
                    City[] citiesArr = (City[]) response;
                    cities = new ArrayList<>(Arrays.asList(citiesArr));
                    updateCitiesUI();
                } else {
                    Utils.showLongToast(this, R.string.failed_loading_cities);
                }
                break;

            case Const.API_EDIT_PROFILE:
                User user = (User) response;
                if (statusCode == Const.SER_CODE_200) {
                    // save the new user obj
                    ActiveUserController activeUserController = new ActiveUserController(this);
                    activeUserController.setUser(user);
                    activeUserController.save();

                    // show msg
                    Utils.showShortToast(this, R.string.profile_updated_successfully);

                    // close activity
                    setResult(RESULT_OK);
                    activity.finish();
                } else {
                    Utils.showLongToast(this, R.string.error_doing_operation);
                }
                break;

            default:
                super.onSuccess(response, statusCode, tag);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();

        if (Const.API_GET_CITIES.equals(tag)) {
            Utils.showLongToast(this, R.string.failed_loading_cities);
        } else {
            super.onFail(ex, statusCode, tag);
        }
    }

    private void updateCitiesUI() {
        // prepare cities
        CityController cityController = new CityController();
        cities = cityController.addDefaultItem(cities, getString(R.string.select_city));

        // set the adapter
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_dropdown_selected, cities);
        adapter.setDropDownViewResource(R.layout.item_dropdown);
        spCity.setAdapter(adapter);

        // select the current user city if possible
        City city = activeUserController.getUser().getCity();
        if (city != null) {
            int position = cityController.getItemPosition(cities, city.getId());
            if (position != -1) {
                spCity.setSelection(position);
            } else {
                cities.add(1, city);
                spCity.setSelection(1);
            }
        }
    }
}
