package com.stadium.player.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stadium.player.R;
import com.stadium.player.dialogs.StadiumsDialog;

/**
 * Created by karam on 7/2/16.
 */
public class CreateTeamActivity extends ParentToolbarActivity {
    private TextView tvFavoriteStadium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_create_team);

        // init views
        tvFavoriteStadium = (TextView) findViewById(R.id.tv_favorite_stadium);

        // add listeners
        tvFavoriteStadium.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_favorite_stadium) {
            // show stadiums dialog
            StadiumsDialog dialog = new StadiumsDialog(this);
            dialog.show();
        } else {
            super.onClick(v);
        }
    }
}
