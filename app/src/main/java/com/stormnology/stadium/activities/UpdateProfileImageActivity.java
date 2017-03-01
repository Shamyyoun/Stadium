package com.stormnology.stadium.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.models.entities.Image;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.BitmapUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.io.File;

/**
 * Created by Shamyyoun on 10/22/16.
 */
public class UpdateProfileImageActivity extends PicPickerActivity {
    private ActiveUserController userController;
    private ImageView ivImage;
    private ImageButton ibEdit;
    private View layoutButtons;
    private Button btnSave;
    private Button btnCancel;

    private File image;
    private String imageEncoded;
    private boolean imageUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_image);

        // create the user controller
        userController = new ActiveUserController(this);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        ibEdit = (ImageButton) findViewById(R.id.ib_edit);
        layoutButtons = findViewById(R.id.layout_buttons);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        ibEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        loadProfileImage();
    }

    private void loadProfileImage() {
        User user = userController.getUser();
        if (!Utils.isNullOrEmpty(user.getImageLink())) {
            Utils.loadImage(this, user.getImageLink(), 0, ivImage);
        } else if (user.getUserImage() != null && !Utils.isNullOrEmpty(user.getUserImage().getContentBase64())) {
            Bitmap bitmap = BitmapUtils.decodeBase64(user.getUserImage().getContentBase64());
            ivImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_edit) {
            chooseImage();
        } else if (v.getId() == R.id.btn_save) {
            save();
        } else if (v.getId() == R.id.btn_cancel) {
            cancel();
        } else {
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
                        setPickerAspects(Const.IMG_ASPECT_X_PROFILE, Const.IMG_ASPECT_Y_PROFILE);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_PROFILE);
                        pickFromGallery(0, true);
                        break;

                    case 1:
                        setPickerAspects(Const.IMG_ASPECT_X_PROFILE, Const.IMG_ASPECT_Y_PROFILE);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_PROFILE);
                        captureFromCamera(0, true);
                        break;
                }
            }
        });
    }

    @Override
    public void onImageReady(int requestCode, File image) {
        this.image = image;
        Picasso.with(this).load(image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(ivImage);
        showButtons(true);
    }

    private void showButtons(boolean show) {
        layoutButtons.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void save() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // encode the image and get the user
        imageEncoded = BitmapUtils.encodeBase64(image);
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.uploadImage(this, this, user.getId(),
                user.getToken(), imageEncoded);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        if (statusCode == Const.SER_CODE_200) {
            showButtons(false);
            Utils.showShortToast(this, R.string.image_updated_successfully);

            // update user image
            User user = userController.getUser();
            Image image = new Image();
            image.setContentBase64(imageEncoded);
            user.setImageLink(null);
            user.setUserImage(image);
            userController.setUser(user);
            userController.save();

            // update the flag
            imageUpdated = true;
        } else {
            Utils.showShortToast(this, R.string.failed_updating_image);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();
        Utils.showShortToast(this, R.string.failed_updating_image);
    }

    private void cancel() {
        showButtons(false);
        loadProfileImage();
    }

    @Override
    public void onBackPressed() {
        if (imageUpdated) {
            setResult(RESULT_OK);
        }
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.scale_fade_exit);
    }
}
