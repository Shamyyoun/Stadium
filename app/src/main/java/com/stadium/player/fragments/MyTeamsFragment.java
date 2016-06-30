package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.stadium.player.R;
import com.stadium.player.adapters.MyTeamsAdapter;

/**
 * Created by karam on 6/30/16.
 */
public class MyTeamsFragment extends ParentFragment {

    private RecyclerView myTeamRecycler;
    private LinearLayout createButton;
    private MyTeamsAdapter myTeamsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_team, container, false);

        myTeamRecycler = (RecyclerView) rootView.findViewById(R.id.myTeams_Recycler);
        myTeamRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
       // myTeamsAdapter = new MyTeamsAdapter(getActivity(), AllTeams);
        myTeamRecycler.setAdapter(myTeamsAdapter);

        createButton = (LinearLayout) rootView.findViewById(R.id.createTeam_btn);
        createButton.setOnClickListener(this);

        return rootView;
    }
}
