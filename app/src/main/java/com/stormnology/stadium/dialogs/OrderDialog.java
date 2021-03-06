package com.stormnology.stadium.dialogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.stormnology.stadium.R;
import com.stormnology.stadium.adapters.RadioButtonsAdapter;
import com.stormnology.stadium.controllers.OrderController;
import com.stormnology.stadium.interfaces.OnCheckableSelectedListener;
import com.stormnology.stadium.models.entities.OrderCriteria;

import java.util.List;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class OrderDialog extends ParentDialog {
    private OrderController orderController;
    private RecyclerView recyclerView;
    private Button btnSubmit;
    private RadioButtonsAdapter adapter;
    private List<OrderCriteria> data;
    private OnCheckableSelectedListener itemSelectedListener;
    private int selectedItemType;

    public OrderDialog(final Context context, List<OrderCriteria> orderCriterias) {
        super(context);
        setContentView(R.layout.dialog_order);
        setTitle(R.string.order_by);
        data = orderCriterias;

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

        // update the ui
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