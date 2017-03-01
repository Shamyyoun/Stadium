package com.stormnology.stadium.activities;

import android.os.Bundle;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.fragments.StadiumInfoFragment;
import com.stormnology.stadium.models.entities.Team;

/*
 * Created by karam on 7/31/16.
 */
public class StadiumInfoActivity extends ParentActivity {
    private StadiumInfoFragment fragment;

    private boolean isReservationAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_info);
        enableBackButton();

        // check saved instance
        if (savedInstanceState == null) {
            // get parameters
            Team selectedTeam = (Team) getIntent().getSerializableExtra(Const.KEY_TEAM);
            int id = getIntent().getIntExtra(Const.KEY_ID, 0);

            // load stadium info fragment with this parameters
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_TEAM, selectedTeam);
            bundle.putInt(Const.KEY_ID, id);
            fragment = new StadiumInfoFragment();
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