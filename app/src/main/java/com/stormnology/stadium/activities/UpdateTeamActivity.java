package com.stormnology.stadium.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.TeamController;
import com.stormnology.stadium.dialogs.ChoosePlayerDialog;
import com.stormnology.stadium.dialogs.ChooseStadiumDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.interfaces.OnUserSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.BitmapUtils;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import java.io.File;

/**
 * Created by karam on 7/2/16.
 */
public class UpdateTeamActivity extends PicPickerActivity {
    private Team team;
    private ActiveUserController userController;
    private TeamController teamController;

    private ImageView ivImage;
    private EditText etTitle;
    private EditText etDesc;
    private Button btnFavoriteStadium;
    private Button btnCaptain;
    private Button btnAssistant;
    private Button btnUpdate;
    private Button btnCancel;

    private File image;
    private ChooseStadiumDialog stadiumsDialog;
    private ChoosePlayerDialog playersDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_update_team);

        // obtain main objects
        team = (Team) getIntent().getSerializableExtra(Const.KEY_TEAM);
        userController = new ActiveUserController(this);
        teamController = new TeamController();

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etTitle = (EditText) findViewById(R.id.et_title);
        etDesc = (EditText) findViewById(R.id.et_desc);
        btnFavoriteStadium = (Button) findViewById(R.id.btn_favorite_stadium);
        btnCaptain = (Button) findViewById(R.id.btn_captain);
        btnAssistant = (Button) findViewById(R.id.btn_assistant);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        ivImage.setOnClickListener(this);
        btnFavoriteStadium.setOnClickListener(this);
        btnCaptain.setOnClickListener(this);
        btnAssistant.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        updateUI();
    }

    private void updateUI() {
        etTitle.setText(team.getName());
        etDesc.setText(team.getDescription());

        // set the fav stadium if possible
        if (team.getPreferStadiumName() != null) {
            String favStadiumStr = getString(R.string.favorite_stadium) + ": "
                    + team.getPreferStadiumName();
            btnFavoriteStadium.setText(favStadiumStr);
        }

        // set the captain if possible
        if (team.getCaptain() != null) {
            String captainStr = getString(R.string.the_captain) + ": "
                    + team.getCaptain().getName();
            btnCaptain.setText(captainStr);
        }

        // set the assistant if possible
        if (team.getAsstent() != null) {
            String assistantStr = getString(R.string.assistant) + ": "
                    + team.getAsstent().getName();
            btnAssistant.setText(assistantStr);
        }

        // load the image if possible
        Utils.loadImage(this, team.getImageLink(), R.drawable.default_image, ivImage);

        // check the active user role
        User user = userController.getUser();
        if (teamController.isCaptain(team, user.getId())) {
            // show change captain button
            btnCaptain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                chooseImage();
                break;

            case R.id.btn_favorite_stadium:
                chooseStadium();
                break;

            case R.id.btn_captain:
                chooseCaptain();
                break;

            case R.id.btn_assistant:
                chooseAssistant();
                break;

            case R.id.btn_update:
                updateTeam();
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
                        setPickerAspects(Const.IMG_ASPECT_X_TEAM, Const.IMG_ASPECT_Y_TEAM);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_TEAM);
                        pickFromGallery(0, true);
                        break;

                    case 1:
                        setPickerAspects(Const.IMG_ASPECT_X_TEAM, Const.IMG_ASPECT_Y_TEAM);
                        setPickerMaxDimen(Const.MAX_IMG_DIMEN_TEAM);
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

    private void chooseStadium() {
        if (stadiumsDialog == null) {
            stadiumsDialog = new ChooseStadiumDialog(this);
            stadiumsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // update the ui
                    Stadium stadium = (Stadium) item;
                    String favStadiumStr = getString(R.string.favorite_stadium)
                            + ": " + stadium.getName();
                    btnFavoriteStadium.setText(favStadiumStr);

                    // set the value in the team object
                    team.setPreferStadiumId(stadium.getId());
                    team.setPreferStadiumName(stadium.getName());
                }
            });
        }

        // set the selected item id
        stadiumsDialog.setSelectedItemId(team.getPreferStadiumId());
        stadiumsDialog.show();
    }

    private void chooseCaptain() {
        createPlayersDialog();

        // set the user selected listener
        playersDialog.setOnUserSelectedListener(new OnUserSelectedListener() {
            @Override
            public void onUserSelected(User user) {
                // check if this user is the assistant
                if (team.getAsstent() != null && user.getId() == team.getAsstent().getId()) {
                    // show msg
                    Utils.showLongToast(activity, R.string.this_player_is_the_assistant_choose_another);
                } else {
                    // update the ui
                    String captainStr = getString(R.string.the_captain)
                            + ": " + user.getName();
                    btnCaptain.setText(captainStr);

                    // set the value in the team object
                    team.setCaptain(user);
                }
            }
        });

        playersDialog.show();
    }

    private void chooseAssistant() {
        createPlayersDialog();

        // set the user selected listener
        playersDialog.setOnUserSelectedListener(new OnUserSelectedListener() {
            @Override
            public void onUserSelected(User user) {
                // check if this user is the captain
                if (team.getCaptain() != null && user.getId() == team.getCaptain().getId()) {
                    // show msg
                    Utils.showLongToast(activity, R.string.this_player_is_the_captain_choose_another);
                } else {
                    // update the ui
                    String assistantStr = getString(R.string.assistant)
                            + ": " + user.getName();
                    btnAssistant.setText(assistantStr);

                    // set the value in the team object
                    team.setAsstent(user);
                }
            }
        });

        playersDialog.show();
    }

    private void createPlayersDialog() {
        if (playersDialog == null) {
            playersDialog = new ChoosePlayerDialog(this, team.getId());
            playersDialog.setRemoveCurrentUser(true);
            playersDialog.setEmptyMsg(getString(R.string.no_players_in_this_team_except_you));
        }
    }

    private void updateTeam() {
        // prepare params
        String title = Utils.getText(etTitle);
        String desc = Utils.getText(etDesc);

        // validate inputs
        if (Utils.isEmpty(title)) {
            etTitle.setError(getString(R.string.required));
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        showProgressDialog();

        // prepare image params if possible
        String encodedImage = null;
        String imageName = null;
        if (image != null) {
            // encode the image
            encodedImage = BitmapUtils.encodeBase64(image);

            // set the suitable image name
            if (team.getTeamImage() != null) {
                imageName = team.getTeamImage().getName();
            } else {
                imageName = "";
            }
        }

        // prepare other params
        int captainId = 0;
        int assistantId = 0;
        if (team.getCaptain() != null) {
            captainId = team.getCaptain().getId();
        }
        if (team.getAsstent() != null) {
            assistantId = team.getAsstent().getId();
        }

        // get the user
        User user = userController.getUser();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.editTeam(this, this, user.getId(),
                user.getToken(), team.getId(), title, desc, encodedImage, imageName,
                captainId, assistantId, team.getPreferStadiumId());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        Team team = (Team) response;
        if (statusCode == Const.SER_CODE_200) {
            // set team obj & show msg
            this.team = team;
            Utils.showShortToast(this, R.string.team_updated_successfully);

            // invalidate the cached team image if changed
            if (image != null && team.getImageLink() != null) {
                Picasso.with(this).invalidate(team.getImageLink());
            }

            // finish the activity
            finishActivity();
        } else {
            String errorMsg = AppUtils.getResponseMsg(this, team);
            Utils.showShortToast(this, errorMsg);
        }
    }

    private void finishActivity() {
        // set result and finish
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_TEAM, team);
        setResult(RESULT_OK, intent);
        finish();
    }
}
