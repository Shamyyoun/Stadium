package com.stadium.player.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stadium.player.R;
import com.stadium.player.adapters.FavoriteStadiumsAdapter;
import com.stadium.player.models.entities.Stadium;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karam on 7/2/16.
 */
public class ChooseStadiumDialog  extends ParentDialog {

    private RecyclerView rvStadiums;
    private Button btnClose;
    private List<Stadium> data;
    private FavoriteStadiumsAdapter favoriteStadiumsAdapter;

    public ChooseStadiumDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_favorite_stadium);

        // init views
        rvStadiums = (RecyclerView) findViewById(R.id.rv_stadiums);
        btnClose = (Button) findViewById(R.id.btn_close);

        // set the recyclerView
        data = getData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvStadiums.setLayoutManager(layoutManager);
        favoriteStadiumsAdapter = new FavoriteStadiumsAdapter(context, data, R.layout.item_favorite_stadium);
        rvStadiums.setAdapter(favoriteStadiumsAdapter);

        // customize the close button
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnCloseClickListener(View.OnClickListener closeClickListener) {
        btnClose.setOnClickListener(closeClickListener);
    }

    // fill data
    private List<Stadium> getData() {
        List<Stadium> stadia = new ArrayList<>();

        String names[] = {"ملعب الاهلي", "ملعب النسور", "ملعب الاهلي", "ملعب النسور","ملعب الاهلي", "ملعب النسور"};

        for (int i = 0; i < names.length; i++) {
            Stadium newStadium = new Stadium(names[i]);
            stadia.add(newStadium);
        }
        return stadia;
    }
}
