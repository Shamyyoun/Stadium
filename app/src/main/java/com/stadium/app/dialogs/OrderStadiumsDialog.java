package com.stadium.app.dialogs;

import android.content.Context;

import com.stadium.app.R;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class OrderStadiumsDialog extends ParentDialog {

    public OrderStadiumsDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_order_stadiums);
        setTitle(R.string.order_by);
    }
}