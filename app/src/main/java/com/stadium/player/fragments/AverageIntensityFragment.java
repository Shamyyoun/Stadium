package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.adapters.AverageIntensityAdapter;
import com.stadium.player.adapters.TeamReservationsAdapter;
import com.stadium.player.models.entities.AverageIntensity;
import com.stadium.player.models.entities.TeamReservations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class AverageIntensityFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private AverageIntensityAdapter adapter;
    private List<AverageIntensity> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_intensity, container, false);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // customize the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        recyclerView.setLayoutManager(layoutManager);
        data = getDummyData();
        adapter = new AverageIntensityAdapter(activity, data, R.layout.item_stadium_intensity);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<AverageIntensity> getDummyData() {
        List<AverageIntensity> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            AverageIntensity item = new AverageIntensity();
            data.add(item);
        }

        return data;
    }
}
