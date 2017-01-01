package com.stadium.app.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stadium.app.ApiRequests;
import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.controllers.ActiveUserController;
import com.stadium.app.dialogs.ChooseStadiumDialog;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;
import com.stadium.app.utils.BitmapUtils;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.io.File;

/**
 * Created by karam on 7/2/16.
 */
public class CreateTeamActivity extends PicPickerActivity {
    private ImageView ivImage;
    private EditText etTitle;
    private EditText etDesc;
    private TextView tvFavoriteStadium;
    private Button btnCreate;
    private Button btnCancel;

    private File image;
    private ChooseStadiumDialog stadiumsDialog;
    private Stadium favoriteStadium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_create_team);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etTitle = (EditText) findViewById(R.id.et_title);
        etDesc = (EditText) findViewById(R.id.et_desc);
        tvFavoriteStadium = (TextView) findViewById(R.id.tv_favorite_stadium);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        ivImage.setOnClickListener(this);
        tvFavoriteStadium.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        // set the image if saved in the state
        if (savedInstanceState != null) {
            image = (File) savedInstanceState.getSerializable("image");
            if (image != null) {
                Picasso.with(this).load(image).placeholder(R.drawable.def_form_image).into(ivImage);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                chooseImage();
                break;

            case R.id.tv_favorite_stadium:
                chooseStadium();
                break;

            case R.id.btn_create:
                createTeam();
                break;

            case R.id.btn_cancel:
                // just finish the activity
                finish();
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
                        setPickerAspects(Const.IMG_ASPECT_X_TEAM, Const.IMG_ASPECT_Y_TEAM);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_TEAM);
                        pickFromGallery(0, true);
                        break;

                    case 1:
                        setPickerAspects(Const.IMG_ASPECT_X_TEAM, Const.IMG_ASPECT_Y_TEAM);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_TEAM);
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
        Picasso.with(this).load(image).placeholder(R.drawable.def_form_image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(ivImage);
    }

    private void removeImage() {
        image = null;
        ivImage.setImageResource(R.drawable.def_form_image);
    }

    private void chooseStadium() {
        if (stadiumsDialog == null) {
            stadiumsDialog = new ChooseStadiumDialog(this);
            stadiumsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    favoriteStadium = (Stadium) item;
                    String favStadiumStr = getString(R.string.favorite_stadium)
                            + ": " + favoriteStadium.getName();
                    Utils.setUnderlined(tvFavoriteStadium, favStadiumStr);
                }
            });
        }

        // set the selected item id if possible
        if (favoriteStadium != null) {
            stadiumsDialog.setSelectedItemId(favoriteStadium.getId());
        }
        stadiumsDialog.show();
    }

    private void createTeam() {
        // prepare params
        String title = Utils.getText(etTitle);
        String desc = Utils.getText(etDesc);

        // validate inputs
        if (Utils.isEmpty(title)) {
            etTitle.setError(getString(R.string.required));
            return;
        }
        if (Utils.isEmpty(desc)) {
            etDesc.setError(getString(R.string.required));
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

        // get the favorite stadium id if possible
        int favStadiumId = favoriteStadium != null ? favoriteStadium.getId() : 0;

        // get the user
        ActiveUserController userController = new ActiveUserController(this);
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.createTeam(this, this, user.getId(),
                user.getToken(), title, desc, favStadiumId, imageEncoded);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        Team team = (Team) response;
        if (statusCode == Const.SER_CODE_200) {
            Utils.showShortToast(this, R.string.team_created_successfully);
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.showLongToast(this, AppUtils.getResponseMsg(this, team));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("image", image);
        super.onSaveInstanceState(outState);
    }
}
