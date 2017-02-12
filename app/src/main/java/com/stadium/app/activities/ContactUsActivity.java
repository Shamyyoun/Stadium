package com.stadium.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.utils.Utils;

/*
 * Created by Shamyyoun on 11/2/16.
 */
public class ContactUsActivity extends ParentActivity {
    private TextView tvEmail;
    private TextView tvTwitter;
    private TextView tvYoutube;
    private TextView tvSnapchat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        enableBackButton();

        // init views
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvTwitter = (TextView) findViewById(R.id.tv_twitter);
        tvYoutube = (TextView) findViewById(R.id.tv_youtube);
        tvSnapchat = (TextView) findViewById(R.id.tv_snapchat);

        // add listeners
        tvEmail.setOnClickListener(this);
        tvTwitter.setOnClickListener(this);
        tvYoutube.setOnClickListener(this);
        tvSnapchat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_email:
                openEmailIntent();
                break;

            case R.id.tv_twitter:
                openTwitterIntent();
                break;

            case R.id.tv_youtube:
                openYoutubeIntent();
                break;

            case R.id.tv_snapchat:
                break;

            default:
                super.onClick(v);
        }
    }

    private void openEmailIntent() {
        Utils.openEmailIntent(this, Const.CON_STADIUM_EMAIL);
    }

    private void openTwitterIntent() {
        Utils.openBrowser(this, Const.CON_STADIUM_TWITTER);
    }

    private void openYoutubeIntent() {
        Utils.openBrowser(this, Const.CON_STADIUM_YOUTUBE);
    }
}
