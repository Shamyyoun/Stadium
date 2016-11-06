package com.stadium.app.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stadium.app.R;
import com.stadium.app.adapters.RadioButtonsAdapter;
import com.stadium.app.controllers.OrderController;
import com.stadium.app.interfaces.OnCheckableSelectedListener;
import com.stadium.app.models.entities.OrderCriteria;

import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class OrderPlayersDialog extends ParentDialog {
    private OrderController orderController;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private RadioButtonsAdapter adapter;
    private List<OrderCriteria> data;
    private OnCheckableSelectedListener itemSelectedListener;
    private int selectedItemType;

    public OrderPlayersDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_order_players);
        setTitle(R.string.order_by);

        // create controllers
        orderController = new OrderController();

        // init views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        // customize the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // add listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        // get data and update the ui
        data = orderController.getPlayersCriterias(context);
        updateUI();
    }

    private void updateUI() {
        adapter = new RadioButtonsAdapter(context, data, R.layout.item_radio_button);
        recyclerView.setAdapter(adapter);

        // select an item if possible
        selectCheckedItem();
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

    public void setSelectedItemType(int selectedItemType) {
        this.selectedItemType = selectedItemType;
    }

    private void selectCheckedItem() {
        int itemPosition = orderController.getItemPosition(data, selectedItemType);
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

    public OrderCriteria getDefaultCriteria() {
        return data.get(0);
    }
}