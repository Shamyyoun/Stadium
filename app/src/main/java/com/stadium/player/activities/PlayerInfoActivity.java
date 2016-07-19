package com.stadium.player.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.stadium.player.R;
import com.stadium.player.adapters.PlayerTeamsAdapter;
import com.stadium.player.adapters.TeamsAdapter;
import com.stadium.player.models.entities.PlayerTeams;
import com.stadium.player.views.DarkenedButton;

import java.util.List;

/**
 * Created by karam on 7/19/16.
 */
public class PlayerInfoActivity extends ParentToolbarActivity {

    private RoundedImageView ivPhoto;
    private TextView tvEvaluation;
    private TextView tvAddress;
    private TextView tvAddPlayer;
    private DarkenedButton btnAdd;

    //RecyclerView Items
    private RecyclerView rvPlayerTeams;
    private List<PlayerTeams> data;
    private PlayerTeamsAdapter playerTeamsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        ivPhoto = (RoundedImageView) findViewById(R.id.iv_photo);
        tvEvaluation = (TextView) findViewById(R.id.tv_evaluation);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvAddPlayer= (TextView) findViewById(R.id.tv_add);
        btnAdd = (DarkenedButton) findViewById(R.id.btn_add);

        rvPlayerTeams = (RecyclerView) findViewById(R.id.rv_teams);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvPlayerTeams.setLayoutManager(layoutManager);
        playerTeamsAdapter = new PlayerTeamsAdapter(this, data, R.layout.item_team);
        rvPlayerTeams.setAdapter(playerTeamsAdapter);

    }
}
