package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.adapters.BigIntensityAdapter;
import com.stadium.player.adapters.SmallIntensityAdapter;
import com.stadium.player.models.entities.BigIntensity;
import com.stadium.player.models.entities.SmallIntensity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class SmallIntensityFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private SmallIntensityAdapter adapter;
    private List<SmallIntensity> data;

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
        adapter = new SmallIntensityAdapter(activity, data, R.layout.item_stadium_intensity);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<SmallIntensity> getDummyData() {
        List<SmallIntensity> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            SmallIntensity item = new SmallIntensity();
            data.add(item);
        }

        return data;
    }
}
