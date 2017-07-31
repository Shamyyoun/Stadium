package com.stormnology.stadium.activities;

import android.os.Bundle;

import com.stormnology.stadium.Const;
import com.stormnology.stadium.R;
import com.stormnology.stadium.fragments.ChallengesFragment;
import com.stormnology.stadium.models.entities.ChallengeInfoHolder;

/**
 * Created by karam on 7/17/16.
 */
public class ChallengesSearchResultActivity extends ParentActivity {
    private ChallengeInfoHolder filter;
    private ChallengesFragment challengesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_search_result);
        enableBackButton();

        // obtain main objects
        filter = (ChallengeInfoHolder) getIntent().getSerializableExtra(Const.KEY_FILTER);

        // load the fragment if possible
        if (savedInstanceState == null) {
            // create the bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable(Const.KEY_FILTER, filter);

            // create and load the fragment
            challengesFragment = new ChallengesFragment();
            challengesFragment.setArguments(bundle);
            loadFragment(R.id.container, challengesFragment);
        }
    }
}
