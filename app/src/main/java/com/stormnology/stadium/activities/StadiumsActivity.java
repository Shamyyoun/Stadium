package com.stormnology.stadium.activities;

import android.os.Bundle;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.fragments.StadiumsFragment;
import com.stormnology.stadium.models.entities.Team;

public class StadiumsActivity extends ParentActivity {
    private Team team; // this is the team object when the user navigates to the add reservations from team info screen
    private StadiumsFragment fragment;
    private boolean isReservationAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadiums);
        enableBackButton();

        // get the team
        team = (Team) getIntent().getSerializableExtra(Const.KEY_TEAM);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // check to load the fragment
        if (savedInstanceState == null) {
            // load the fragment
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_TEAM, team);
            bundle.putString(Const.KEY_TOOLBAR_TITLE, getString(R.string.add_reservations));
            fragment = new StadiumsFragment();
            fragment.setArguments(bundle);
            loadFragment(R.id.container, fragment);
        }
    }

    public void onReservationAdded() {
        isReservationAdded = true;
    }

    @Override
    public void onBackPressed() {
        if (isReservationAdded) {
            setResult(RESULT_OK);
        }

        finish();
    }
}