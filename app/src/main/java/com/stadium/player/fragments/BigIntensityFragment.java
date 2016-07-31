package com.stadium.player.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stadium.player.R;
import com.stadium.player.adapters.AverageIntensityAdapter;
import com.stadium.player.adapters.BigIntensityAdapter;
import com.stadium.player.dialogs.ChooseTeamDialog;
import com.stadium.player.interfaces.OnItemClickListener;
import com.stadium.player.models.entities.AverageIntensity;
import com.stadium.player.models.entities.BigIntensity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/31/16.
 */
public class BigIntensityFragment extends ParentFragment {

    private RecyclerView recyclerView;
    private BigIntensityAdapter adapter;
    private List<BigIntensity> data;

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
        adapter = new BigIntensityAdapter(activity, data, R.layout.item_stadium_intensity);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open teams dialog
                ChooseTeamDialog dialog = new ChooseTeamDialog(activity);
                dialog.show();
            }
        });
        return rootView;
    }

    private List<BigIntensity> getDummyData() {
        List<BigIntensity> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BigIntensity item = new BigIntensity();
            data.add(item);
        }

        return data;
    }
}
