package com.stadium.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.CityController;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 6/29/16.
 */
public class UpdateProfileActivity extends ParentActivity {
    private ActiveUserController userController;
    private ImageView ivImage;
    private EditText etName;
    private EditText etAge;
    private Spinner spCity;
    private EditText etPhone;
    private EditText etPosition;
    private EditText etEmail;
    private EditText etBio;
    private Button btnUpdate;

    private List<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // create the user controller
        userController = new ActiveUserController(this);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etName = (EditText) findViewById(R.id.et_name);
        etAge = (EditText) findViewById(R.id.et_age);
        spCity = (Spinner) findViewById(R.id.sp_city);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPosition = (EditText) findViewById(R.id.et_position);
        etEmail = (EditText) findViewById(R.id.et_email);
        etBio = (EditText) findViewById(R.id.et_bio);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        // add listeners
        btnUpdate.setOnClickListener(this);

        // update and load cities
        updateCitiesUI();
        loadCities();

        // update ui from saved user
        updateUI();

        etName.requestFocus();
    }

    private void updateUI() {
        User user = userController.getUser();

        // set basic info
        etName.setText(user.getName());
        etAge.setText("" + user.getAge());
        etPhone.setText(user.getPhone());
        etPosition.setText(user.getPosition());
        etEmail.setText(user.getEmail().trim());
        etBio.setText(user.getBio());

        // set the city
        City city = user.getCity();
        if (city != null) {
            CityController cityController = new CityController();
            if (cities != null) {
                int position = cityController.getItemPosition(cities, city.getId());
                if (position != -1) {
                    spCity.setSelection(position);
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
            case R.id.btn_update:
                update();
                break;

            default:
                super.onClick(v);
        }
    }

    private void update() {
        // prepare params
        int age = Utils.getInt(etAge);
        City city = (City) spCity.getSelectedItem();
        String phone = Utils.getText(etPhone);
        String position = Utils.getText(etPosition);
        String email = Utils.getText(etEmail);
        String bio = Utils.getText(etBio);

        // validate inputs
        if (Utils.isEmpty(Utils.getText(etAge))) {
            etAge.setError(getString(R.string.required));
            return;
        } else if (age <= 0) {
            etAge.setError(getString(R.string.invalid_value));
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
        User user = userController.getUser();
        ConnectionHandler connectionHandler = ApiRequests.editProfile(this, this, user.getId(),
                user.getToken(), age, city, phone, position, email, bio);
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
                    ActiveUserController userController = new ActiveUserController(this);
                    userController.setUser(user);
                    userController.save();

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
        City city = userController.getUser().getCity();
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
