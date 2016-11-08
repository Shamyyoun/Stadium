package com.stadium.app.activities;

import android.os.Bundle;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.fragments.PlayersFragment;
import com.stadium.app.models.entities.Team;

public class PlayersActivity extends ParentActivity {
    private Team team;
    private PlayersFragment fragment;
    private boolean isPlayersAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
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
            fragment = new PlayersFragment();
            fragment.setArguments(bundle);
            loadFragment(R.id.container, fragment);
        }
    }

    public void onPlayerAdded() {
        isPlayersAdded = true;
    }

    @Override
    public void onBackPressed() {
        if (isPlayersAdded) {
            setResult(RESULT_OK);
        }

        finish();
    }
}