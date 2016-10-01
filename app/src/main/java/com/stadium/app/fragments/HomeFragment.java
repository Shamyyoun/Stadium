package com.stadium.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stadium.app.R;
import com.stadium.app.activities.CreateTeamActivity;
import com.stadium.app.controllers.UserController;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

/**
 * Created by Shamyyoun on 7/2/16.
 */
public class HomeFragment extends ParentToolbarFragment {
    private UserController userController;
    private ImageView ivImage;
    private TextView tvRating;
    private TextView tvName;
    private Button btnCreateTeam;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.home);
        createOptionsMenu(R.menu.menu_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // create the user controller
        userController = new UserController(activity);

        // init views
        ivImage = (ImageView) findViewById(R.id.iv_image);
        tvRating = (TextView) findViewById(R.id.tv_rating);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnCreateTeam = (Button) findViewById(R.id.btn_create_team);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // add listeners
        btnCreateTeam.setOnClickListener(this);

        // update the ui
        updateUserUI();

        return rootView;
    }

    private void updateUserUI() {
        User user = userController.getUser();
        tvName.setText(userController.getNamePosition());
        tvRating.setText("" + user.getRate());

        Utils.loadImage(activity, user.getImageLink(), R.drawable.default_image, ivImage);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_create_team) {
            // open create team activity
            Intent intent = new Intent(activity, CreateTeamActivity.class);
            startActivity(intent);
        } else {
            super.onClick(v);
        }
    }
}
