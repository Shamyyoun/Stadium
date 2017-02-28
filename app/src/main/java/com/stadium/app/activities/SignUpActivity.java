package com.stadium.app.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.controllers.CityController;
import com.stadium.app.controllers.ParseController;
import com.stadium.app.dialogs.PolicyDialog;
import com.stadium.app.dialogs.VerifyAccountDialog;
import com.stadium.app.interfaces.ConfirmListener;
import com.stadium.app.interfaces.OnVerifyAccountListener;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.BitmapUtils;
import com.stadium.app.utils.DatePickerFragment;
import com.stadium.app.utils.DateUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karam on 6/29/16.
 */
public class SignUpActivity extends PicPickerActivity implements ConfirmListener {
    private static final String DISPLAYED_DATE_FORMAT = "yyyy/M/d";

    private ActiveUserController userController;
    private ParseController parseController;

    private ImageView ivImage;
    private EditText etName;
    private Button btnBirthdate;
    private Spinner spCity;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etRePassword;
    private CheckBox cbPolicy;
    private TextView tvPolicy;
    private Button btnRegister;
    private TextView tvLogin;

    private List<City> cities;
    private File image;
    private DatePickerFragment datePickerFragment;
    private PolicyDialog policyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // obtain main objects
        userController = new ActiveUserController(this);
        parseController = new ParseController(this);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etName = (EditText) findViewById(R.id.et_name);
        btnBirthdate = (Button) findViewById(R.id.btn_birthdate);
        spCity = (Spinner) findViewById(R.id.sp_city);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        etRePassword = (EditText) findViewById(R.id.et_re_password);
        cbPolicy = (CheckBox) findViewById(R.id.cb_policy);
        tvPolicy = (TextView) findViewById(R.id.tv_policy);
        btnRegister = (Button) findViewById(R.id.btn_register);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        // add listeners
        ivImage.setOnClickListener(this);
        btnBirthdate.setOnClickListener(this);
        tvPolicy.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        updateCitiesUI();
        loadCities();

        // set the image if saved in the state
        if (savedInstanceState != null) {
            image = (File) savedInstanceState.getSerializable("image");
            if (image != null) {
                Picasso.with(this).load(image).placeholder(R.drawable.def_user_form_image).into(ivImage);
            }
        }
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
            case R.id.iv_image:
                chooseImage();
                break;

            case R.id.btn_birthdate:
                chooseBirthdate();
                break;

            case R.id.tv_policy:
                openPolicyDialog();
                break;

            case R.id.btn_register:
                register();
                break;

            case R.id.tv_login:
                endActivity();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseImage() {
        // prepare the appropriate array
        String[] options;
        if (image == null) {
            options = new String[]{
                    getString(R.string.from_gallery),
                    getString(R.string.from_camera)
            };
        } else {
            options = new String[]{
                    getString(R.string.from_gallery),
                    getString(R.string.from_camera),
                    getString(R.string.remove_image)
            };
        }

        // show list dialog
        DialogUtils.showListDialog(this, options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // switch the selected item
                switch (which) {
                    case 0:
                        setPickerAspects(Const.IMG_ASPECT_X_PROFILE, Const.IMG_ASPECT_Y_PROFILE);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_PROFILE);
                        pickFromGallery(0, true);
                        break;

                    case 1:
                        setPickerAspects(Const.IMG_ASPECT_X_PROFILE, Const.IMG_ASPECT_Y_PROFILE);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_PROFILE);
                        captureFromCamera(0, true);
                        break;

                    case 2:
                        removeImage();
                        break;
                }
            }
        });
    }

    @Override
    public void onImageReady(int requestCode, File image) {
        this.image = image;
        Picasso.with(this).load(image).placeholder(R.drawable.def_user_form_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(ivImage);
    }

    private void removeImage() {
        image = null;
        ivImage.setImageResource(R.drawable.def_user_form_image);
    }

    private void chooseBirthdate() {
        // create the date picker fragment if required
        if (datePickerFragment == null) {
            datePickerFragment = new DatePickerFragment();

            // set min & max dates
            datePickerFragment.setMinDate(Const.USER_MIN_BIRTHDATE, Const.SER_DATE_FORMAT);
            datePickerFragment.setMaxDate(Const.USER_MAX_BIRTHDATE, Const.SER_DATE_FORMAT);

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

    private void openPolicyDialog() {
        // create the dialog if required
        if (policyDialog == null) {
            policyDialog = new PolicyDialog(this);
            policyDialog.setConfirmListener(this);
        }

        // show it
        policyDialog.show();
    }

    // policy accepted method
    @Override
    public void onAccept() {
        cbPolicy.setChecked(true);
    }

    // policy decline method
    @Override
    public void onDecline() {
        cbPolicy.setChecked(false);
    }

    private void register() {
        // prepare params
        String name = Utils.getText(etName);
        String birthdate = DateUtils.formatDate(btnBirthdate.getText().toString(), DISPLAYED_DATE_FORMAT, Const.SER_DATE_FORMAT);
        City city = (City) spCity.getSelectedItem();
        String phone = Utils.getText(etPhone);
        String password = etPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();

        // validate conditions
        if (Utils.isEmpty(name)) {
            etName.setError(getString(R.string.required));
            return;
        }
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
        if (Utils.isEmpty(password)) {
            etPassword.setError(getString(R.string.required));
            return;
        }
        if (password.length() < Const.USER_PASSWORD_MIN_CHARS) {
            etPassword.setError(getString(R.string.too_short_password));
            return;
        }
        if (Utils.isEmpty(rePassword)) {
            etRePassword.setError(getString(R.string.required));
            return;
        }
        if (!password.equals(rePassword)) {
            etRePassword.setError(getString(R.string.passwords_dont_match));
            return;
        }
        if (!cbPolicy.isChecked()) {
            Utils.showShortToast(this, R.string.you_must_accept_terms_and_conditions);
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // encode image if possible
        String imageEncoded = null;
        if (image != null) {
            imageEncoded = BitmapUtils.encodeBase64(image);
        }

        // send request
        ConnectionHandler connectionHandler = ApiRequests.createUser(this, this, name, birthdate, city, phone,
                password, imageEncoded, User.TYPE_PLAYER);
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
                    // verify account first
                    showVerifyAccountDialog(user);
                } else {
                    Utils.showLongToast(this, AppUtils.getResponseMsg(this, user));
                }
                break;

            default:
                super.onSuccess(response, statusCode, tag);
        }
    }

    private void showVerifyAccountDialog(final User user) {
        // create the dialog and add listeners
        VerifyAccountDialog dialog = new VerifyAccountDialog(this, user.getId());
        dialog.setOnVerifyAccountListener(new OnVerifyAccountListener() {
            @Override
            public void onAccountVerified() {
                // change status and continue handling the login
                user.setValidationStatus(true);
                handleSuccessfulSignup(user);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                endActivity();
            }
        });

        // show the dialog
        dialog.show();
    }

    private void handleSuccessfulSignup(User user) {
        // set user password
        String password = Utils.getText(etPassword);
        user.setPassword(password);

        // save it
        userController.setUser(user);
        userController.save();

        // install parse
        parseController.install(user.getId(), user.getChannels());

        // goto main activity
        Intent intent = new Intent(this, PlayerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
    }

    private void endActivity() {
        onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("image", image);
        super.onSaveInstanceState(outState);
    }
}
