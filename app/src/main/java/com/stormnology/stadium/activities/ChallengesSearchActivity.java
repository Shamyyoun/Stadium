package com.stormnology.stadium.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.dialogs.ChooseChallengeDayDialog;
import com.stormnology.stadium.dialogs.ChooseChallengeTimeDialog;
import com.stormnology.stadium.dialogs.ChooseChallengeTypeDialog;
import com.stormnology.stadium.dialogs.ChooseFromChallengeStadiumDialog;
import com.stormnology.stadium.dialogs.ChooseFromChallengeTeamsDialog;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.models.Checkable;
import com.stormnology.stadium.models.entities.ChallengeDay;
import com.stormnology.stadium.models.entities.ChallengeTime;
import com.stormnology.stadium.models.entities.ChallengeType;
import com.stormnology.stadium.models.entities.ChallengeInfoHolder;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class ChallengesSearchActivity extends ParentActivity {
    private ChallengeInfoHolder filter;
    private View layoutContent;
    private Button btnType;
    private Button btnTeam;
    private Button btnPlace;
    private Button btnDay;
    private Button btnTime;
    private Button btnSearch;

    private Rect contentLayoutRect; // to handle the outside click
    private ChooseChallengeTypeDialog typesDialog;
    private ChooseFromChallengeTeamsDialog teamsDialog;
    private ChooseFromChallengeStadiumDialog stadiumsDialog;
    private ChooseChallengeDayDialog daysDialog;
    private ChooseChallengeTimeDialog timesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_search);
        customizeLayoutParams();

        // customize the toolbar
        setToolbarIcon(R.drawable.white_close_icon);
        enableBackButton();

        // get the filter
        filter = (ChallengeInfoHolder) getIntent().getSerializableExtra(Const.KEY_FILTER);

        // init views
        layoutContent = findViewById(R.id.layout_content);
        btnType = (Button) findViewById(R.id.btn_type);
        btnTeam = (Button) findViewById(R.id.btn_team);
        btnPlace = (Button) findViewById(R.id.btn_place);
        btnDay = (Button) findViewById(R.id.btn_day);
        btnTime = (Button) findViewById(R.id.btn_time);
        btnSearch = (Button) findViewById(R.id.btn_search);

        // add listeners
        btnType.setOnClickListener(this);
        btnTeam.setOnClickListener(this);
        btnPlace.setOnClickListener(this);
        btnDay.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        // create the content rect
        contentLayoutRect = new Rect();

        // check filter to update the ui or just create a new one
        if (filter != null) {
            updateUI();
        } else {
            filter = new ChallengeInfoHolder();
        }
    }

    private void customizeLayoutParams() {
        // customize the layout params:
        // set the activity width and height to match parent and its gravity to the top
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
    }

    private void updateUI() {
        updateTypeUI();
        updateTeamUI();
        updatePlaceUI();
        updateDayUI();
        updateTimeUI();
    }

    private void updateTypeUI() {
        if (filter.getType() != null) {
            String str = getString(R.string.type) + ": " + filter.getType().toString();
            btnType.setText(str);
        } else {
            btnType.setText(R.string.challenge_type);
        }
    }

    private void updateTeamUI() {
        if (filter.getHostTeam() != null) {
            String str = getString(R.string.the_team) + ": " + filter.getHostTeam().toString();
            btnTeam.setText(str);
        } else {
            btnTeam.setText(R.string.the_team);
        }
    }

    private void updatePlaceUI() {
        if (filter.getPlace() != null) {
            String str = getString(R.string.place) + ": " + filter.getPlace().toString();
            btnPlace.setText(str);
        } else {
            btnPlace.setText(R.string.place);
        }
    }

    private void updateDayUI() {
        if (filter.getDay() != null) {
            String str = getString(R.string.day) + ": " + filter.getDay().toString();
            btnDay.setText(str);
        } else {
            btnDay.setText(R.string.day);
        }
    }

    private void updateTimeUI() {
        if (filter.getTime() != null) {
            String str = getString(R.string.time) + ": " + filter.getTime().toString();
            btnTime.setText(str);
        } else {
            btnTime.setText(R.string.time);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_type:
                chooseType();
                break;

            case R.id.btn_team:
                chooseTeam();
                break;

            case R.id.btn_place:
                choosePlace();
                break;

            case R.id.btn_day:
                chooseDay();
                break;

            case R.id.btn_time:
                chooseTime();
                break;

            case R.id.btn_search:
                openSearchResultActivity();
                break;

            default:
                super.onClick(v);
        }
    }

    private void chooseType() {
        if (typesDialog == null) {
            typesDialog = new ChooseChallengeTypeDialog(this);
            typesDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    ChallengeType type = (ChallengeType) item;
                    filter.setType(type);
                    updateTypeUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getType() != null) {
            typesDialog.setSelectedItemId(filter.getType().getId());
        }

        typesDialog.show();
    }

    private void chooseTeam() {
        if (teamsDialog == null) {
            teamsDialog = new ChooseFromChallengeTeamsDialog(this);
            teamsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Team team = (Team) item;
                    filter.setHostTeam(team);
                    updateTeamUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getHostTeam() != null) {
            teamsDialog.setSelectedItemId(filter.getHostTeam().getId());
        }

        teamsDialog.show();
    }

    private void choosePlace() {
        if (stadiumsDialog == null) {
            stadiumsDialog = new ChooseFromChallengeStadiumDialog(this);
            stadiumsDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    Stadium stadium = (Stadium) item;
                    filter.setPlace(stadium);
                    updatePlaceUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getPlace() != null) {
            stadiumsDialog.setSelectedItemId(filter.getPlace().getId());
        }

        stadiumsDialog.show();
    }

    private void chooseDay() {
        if (daysDialog == null) {
            daysDialog = new ChooseChallengeDayDialog(this);
            daysDialog.setAddDefaultItem(true);
            daysDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the day item
                    ChallengeDay challengeDay = (ChallengeDay) item;
                    if (challengeDay.getName().equals(getString(R.string.all_days))) {
                        // all days item
                        filter.setDay(null);
                    } else {
                        filter.setDay(challengeDay.getName());
                    }
                    updateDayUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getDay() != null) {
            daysDialog.setSelectedItem(filter.getDay());
        }

        daysDialog.show();
    }

    private void chooseTime() {
        if (timesDialog == null) {
            timesDialog = new ChooseChallengeTimeDialog(this);
            timesDialog.setAddDefaultItem(true);
            timesDialog.setOnItemSelectedListener(new OnCheckableSelectedListener() {
                @Override
                public void onCheckableSelected(Checkable item) {
                    // check the time item
                    ChallengeTime challengeTime = (ChallengeTime) item;
                    if (challengeTime.getName().equals(getString(R.string.all_times))) {
                        // all times item
                        filter.setTime(null);
                    } else {
                        filter.setTime(challengeTime.getName());
                    }
                    updateTimeUI();
                }
            });
        }

        // check to select item if possible
        if (filter.getTime() != null) {
            timesDialog.setSelectedItem(filter.getTime());
        }

        timesDialog.show();
    }

    private void openSearchResultActivity() {
        Intent intent = new Intent(activity, ChallengesSearchResultActivity.class);
        intent.putExtra(Const.KEY_FILTER, filter);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // set result
        Intent intent = new Intent();
        intent.putExtra(Const.KEY_FILTER, filter);
        setResult(RESULT_OK, intent);

        // and finish
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.top_translate_exit);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // get x, y
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();

        // check if this point is outside the content layout
        layoutContent.getDrawingRect(contentLayoutRect);
        if (ev.getAction() == MotionEvent.ACTION_UP
                && !contentLayoutRect.contains(x, y)) {
            onBackPressed();
        }

        return super.dispatchTouchEvent(ev);
    }
}
