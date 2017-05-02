package com.stormnology.stadium.activities;

import android.os.Bundle;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.fragments.PlayersFragment;
import com.stormnology.stadium.models.entities.Team;

public class PlayersActivity extends ParentActivity {
    private Team team; // this is the team object when the user navigates to the add players from team info screen
    private PlayersFragment fragment;
    private boolean arePlayersAdded = false;

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
            bundle.putString(Const.KEY_TOOLBAR_TITLE, getString(R.string.add_players));
            fragment = new PlayersFragment();
            fragment.setArguments(bundle);
            loadFragment(R.id.container, fragment);
        }
    }

    public void onPlayerAdded() {
        arePlayersAdded = true;
    }

    @Override
    public void onBackPressed() {
        if (arePlayersAdded) {
            setResult(RESULT_OK);
        }

        finish();
    }
}