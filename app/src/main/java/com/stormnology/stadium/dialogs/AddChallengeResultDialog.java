package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.ApiRequests;
import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.ChallengeController;
import com.stormnology.stadium.interfaces.OnChallengeUpdatedListener;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;


/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class AddChallengeResultDialog extends ParentDialog {
    private Challenge challenge;
    private ChallengeController challengeController;
    private ActiveUserController activeUserController;

    private TextView tvHostName;
    private ImageView ivHostImage;
    private EditText etHostScore;
    private TextView tvGuestName;
    private ImageView ivGuestImage;
    private EditText etGuestScore;
    private Button btnSubmit;

    private OnChallengeUpdatedListener onChallengeUpdatedListener;

    public AddChallengeResultDialog(Context context, Challenge challenge) {
        super(context);

        // obtain main objects
        this.challenge = challenge;
        challengeController = new ChallengeController();
        activeUserController = new ActiveUserController(context);

        // customize dialog
        setContentView(R.layout.dialog_add_challenge_result);
        setTitle(R.string.add_result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init views
        tvHostName = (TextView) findViewById(R.id.tv_host_name);
        ivHostImage = (ImageView) findViewById(R.id.iv_host_image);
        etHostScore = (EditText) findViewById(R.id.et_host_score);
        tvGuestName = (TextView) findViewById(R.id.tv_guest_name);
        ivGuestImage = (ImageView) findViewById(R.id.iv_guest_image);
        etGuestScore = (EditText) findViewById(R.id.et_guest_score);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // add listeners
        btnSubmit.setOnClickListener(this);
        etGuestScore.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addResult();
                    return true;
                }
                return false;
            }
        });

        // update the ui
        updateUI();
    }

    private void updateUI() {
        // set host team info
        Team hostTeam = challenge.getHostTeam();
        tvHostName.setText(challengeController.getHostTeamName(challenge));
        Utils.loadImage(context, hostTeam.getImageLink(), R.drawable.default_image, ivHostImage);

        // set guest team info
        Team guestTeam = challenge.getGuestTeam();
        tvGuestName.setText(challengeController.getGuestTeamName(context, challenge));
        Utils.loadImage(context, guestTeam.getImageLink(), R.drawable.default_image, ivGuestImage);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            addResult();
        } else {
            super.onClick(v);
        }
    }

    private void addResult() {
        // prepare inputs
        int hostScore = Utils.convertToInt(Utils.getText(etHostScore));
        int guestScore = Utils.convertToInt(Utils.getText(etGuestScore));

        // validate inputs
        if (Utils.isEmpty(etHostScore) || Utils.isEmpty(etGuestScore)) {
            Utils.showShortToast(context, R.string.fill_result);
            return;
        }

        hideKeyboard();

        // check internet connection
        if (!Utils.hasConnection(context)) {
            Utils.showShortToast(context, R.string.no_internet_connection);
            return;
        }

        showProgressView();

        // prepare objects
        User user = activeUserController.getUser();
        Challenge challenge = this.challenge.cloneObject();

        // send request
        ConnectionHandler connectionHandler = ApiRequests.addChallengeResult(context, this,
                user.getId(), user.getToken(), challenge, hostScore, guestScore);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressView();

        // check status code
        if (statusCode == Const.SER_CODE_200) {
            // set the new challenge & fire the listener
            challenge = (Challenge) response;
            fireListener();

            // show msg and dismiss
            Utils.showShortToast(context, R.string.added_successfully);
            dismiss();
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(context, response, R.string.failed_adding_result);
            Utils.showShortToast(context, msg);
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressView();
        Utils.showShortToast(context, R.string.failed_adding_result);
    }

    private void fireListener() {
        if (onChallengeUpdatedListener != null) {
            onChallengeUpdatedListener.onChallengeUpdated(challenge);
        }
    }

    public void setOnChallengeUpdatedListener(OnChallengeUpdatedListener onChallengeUpdatedListener) {
        this.onChallengeUpdatedListener = onChallengeUpdatedListener;
    }
}
