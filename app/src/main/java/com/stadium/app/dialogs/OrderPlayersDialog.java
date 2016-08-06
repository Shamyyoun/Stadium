package com.stadium.app.dialogs;

import android.content.Context;

import com.stadium.app.R;

/**
 * Created by Shamyyoun on 6/28/16.
 */
public class OrderPlayersDialog extends ParentDialog {

    public OrderPlayersDialog(final Context context) {
        super(context);
        setContentView(R.layout.dialog_order_players);
        setTitle(R.string.order_by);
    }
}