package com.stadium.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.dialogs.StadiumsDialog;

/**
 * Created by karam on 7/2/16.
 */
public class CreateTeamActivity extends ParentToolbarActivity {
    private ImageView ivImage;
    private EditText etTitle;
    private EditText etDesc;
    private TextView tvFavoriteStadium;
    private Button btnCreate;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();

        setContentView(R.layout.activity_create_team);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        etTitle = (EditText) findViewById(R.id.et_title);
        etDesc = (EditText) findViewById(R.id.et_desc);
        tvFavoriteStadium = (TextView) findViewById(R.id.tv_favorite_stadium);
        btnCreate = (Button) findViewById(R.id.btn_create);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        tvFavoriteStadium.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_favorite_stadium:
                // show stadiums dialog
                StadiumsDialog dialog = new StadiumsDialog(this);
                dialog.show();

                break;

            case R.id.btn_create:
                createTeam();
                break;

            case R.id.btn_cancel:
                // just finish the activity
                finish();
                break;

            default:
                super.onClick(v);
        }
    }

    private void createTeam() {

    }
}
