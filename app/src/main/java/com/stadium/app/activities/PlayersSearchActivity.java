package com.stadium.app.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.stadium.app.R;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class PlayersSearchActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_search);
        customizeLayoutParams();

        // customize the toolbar
        setToolbarIcon(R.drawable.white_close_icon);
        enableBackButton();
    }

    private void customizeLayoutParams() {
        // customize the layout params:
        // set the activity width to match parent and its gravity to the top
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.top_translate_exit);
    }
}
