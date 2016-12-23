package com.stadium.app.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stadium.app.R;
import com.stadium.app.adapters.RadioButtonsAdapter;
import com.stadium.app.controllers.DayController;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.models.entities.Day;

import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class ChooseDayDialog extends ParentDialog {
    private DayController dayController;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private RadioButtonsAdapter adapter;
    private List<Day> data;
    private OnCheckableSelectedListener itemSelectedListener;
    private int selectedItemId;

    public ChooseDayDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_choose_day);
        setTitle(R.string.days);

        // create controllers
        dayController = new DayController();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // add click listeners
        btnSubmit.setOnClickListener(this);

        // get the days list and update ui
        data = dayController.getDays(context);
        updateUI();
    }

    private void updateUI() {
        adapter = new RadioButtonsAdapter(context, data, R.layout.item_radio_button);
        recyclerView.setAdapter(adapter);

        // select an item if possible
        selectCheckedItem();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            onSubmit();
        } else {
            super.onClick(v);
        }
    }

    private void onSubmit() {
        if (adapter != null && data != null && itemSelectedListener != null) {
            itemSelectedListener.onCheckableSelected(data.get(adapter.getSelectedItemPosition()));
        }

        dismiss();
    }

    public void setOnItemSelectedListener(OnCheckableSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public void setSelectedItemId(int selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    private void selectCheckedItem() {
        int itemPosition = dayController.getItemPosition(data, selectedItemId);
        if (itemPosition != -1) {
            adapter.setSelectedItem(itemPosition);
        }
    }

    @Override
    public void show() {
        super.show();

        if (data != null && adapter != null) {
            selectCheckedItem();
        }
    }
}