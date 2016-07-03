package com.stadium.player.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.stadium.player.R;
import com.stadium.player.dialogs.ChooseStadiumDialog;

/**
 * Created by karam on 7/2/16.
 */
public class CreateTeamActivity extends ParentActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        //Init EditText
        et_teamName = (EditText) findViewById(R.id.et_team_name);
        et_teamDescription = (EditText) findViewById(R.id.et_team_details);
        // Init button in creat team screen
        createTeam = (Button) findViewById(R.id.btn_create_team);
        cancel = (Button) findViewById(R.id.btn_cancel_team);
        // Init Linear that open choose stadium
        chooseStadium = (LinearLayout) findViewById(R.id.tv_choose_favorite_stadium);
        // Add click listener to activity components
        createTeam.setOnClickListener(this);
        cancel.setOnClickListener(this);
        chooseStadium.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){

            case R.id.btn_create_team:
                // get data from editText and put in Variables
                teamName = et_teamName.getText().toString();
                teamDescription = et_teamDescription.getText().toString();

                break;

            case R.id.btn_cancel_team:

                startActivity(new Intent(this , MainActivity.class));

                break;

            case R.id.tv_choose_favorite_stadium:

                ChooseStadiumDialog stadiumDialog = new ChooseStadiumDialog(this);
                stadiumDialog.show();

                break;
        }
    }
}
