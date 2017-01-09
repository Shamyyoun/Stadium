package com.stadium.app.activities;

import android.os.Bundle;
import android.widget.ImageView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.utils.Utils;

/**
 * Created by Shamyyoun on 10/22/16.
 */
public class ViewImageActivity extends ParentActivity {
    private String imageUrl;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        // load the image
        imageUrl = getIntent().getStringExtra(Const.KEY_IMAGE_URL);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        Utils.loadImage(this, imageUrl, 0, ivImage);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.scale_fade_exit);
    }
}
