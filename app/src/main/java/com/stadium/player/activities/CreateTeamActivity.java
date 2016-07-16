package com.stadium.player.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.stadium.player.R;
import com.stadium.player.views.DarkenedImageButton;

/**
 * Created by karam on 7/2/16.
 */
public class CreateTeamActivity extends ParentToolbarActivity {

    //Buttons
    private Button createTeam;
    private Button cancel;
    //Linear
    private LinearLayout chooseStadium;
    //EditText
    private EditText et_teamName;
    private EditText et_teamDescription;
    // variable to save user inputs
    private String teamName;
    private String teamDescription;
    //ImageButton
    private DarkenedImageButton ib_teamImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_create_team);
    }
}
