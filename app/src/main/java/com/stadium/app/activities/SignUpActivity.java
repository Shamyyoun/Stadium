package com.stadium.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.CityController;
import com.stadium.app.controllers.UserController;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 6/29/16.
 */
public class SignUpActivity extends ParentActivity {
    private EditText etName;
    private EditText etAge;
    private Spinner spCity;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etRePassword;
    private Button btnRegister;
    private TextView tvLogin;

    private List<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // init views
        etName = (EditText) findViewById(R.id.et_name);
        etAge = (EditText) findViewById(R.id.et_age);
        spCity = (Spinner) findViewById(R.id.sp_city);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        etRePassword = (EditText) findViewById(R.id.et_re_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        // add listeners
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        etRePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    register();
                    return true;
                }
                return false;
            }
        });


        updateCitiesUI();
        loadCities();
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
            case R.id.btn_register:
                register();
                break;

            case R.id.tv_login:
                finish();
                break;

            default:
                super.onClick(v);
        }
    }

    private void register() {
        // prepare params
        String name = Utils.getText(etName);
        int age = Utils.getInt(etAge);
        City city = (City) spCity.getSelectedItem();
        String phone = Utils.getText(etPhone);
        String password = etPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();

        // validate inputs
        if (Utils.isEmpty(name)) {
            etName.setError(getString(R.string.required));
            return;
        }
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
        if (Utils.isEmpty(password)) {
            etPassword.setError(getString(R.string.required));
            return;
        } else if (!password.equals(rePassword)) {
            etRePassword.setError(getString(R.string.passwords_dont_match));
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
        ConnectionHandler connectionHandler = ApiRequests.createUser(this, this, name, age, city, phone,
                password, User.TYPE_PLAYER);
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

            case Const.API_CREATE_USER:
                User user = (User) response;
                if (statusCode == Const.SER_CODE_200) {
                    // save it
                    UserController userController = new UserController(this);
                    userController.setUser(user);
                    userController.save();

                    // goto main activity
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Utils.showLongToast(this, AppUtils.getResponseError(this, user));
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
        CityController cityController = new CityController(this);
        cities = cityController.addDefaultItem(cities);

        // set the adapter
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_dropdown_selected, cities);
        adapter.setDropDownViewResource(R.layout.item_dropdown);
        spCity.setAdapter(adapter);
    }
}
