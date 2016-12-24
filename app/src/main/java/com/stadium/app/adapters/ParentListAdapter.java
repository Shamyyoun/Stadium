package com.stadium.app.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.stadium.app.R;
import com.stadium.app.interfaces.OnItemRemovedListener;
import com.stadium.app.utils.DialogUtils;
import com.stadium.app.utils.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 5/11/16.
 */
public abstract class ParentListAdapter<Item> extends ArrayAdapter<Item> {
    protected Context context;
    protected List<Item> data;
    protected int layoutId;
    protected LayoutInflater layoutInflater;
    protected OnItemRemovedListener itemRemovedListener;
    protected ProgressDialog progressDialog;

    public ParentListAdapter(Context context, List<Item> data, int layoutId) {
        super(context, layoutId, data);
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ParentListAdapter(Context context, Item[] data, int layoutId) {
        super(context, layoutId, data);
        this.context = context;
        this.layoutId = layoutId;
        this.data = Arrays.asList(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    protected void logE(String msg) {
        Utils.logE(msg);
    }

    protected String getString(int resId) {
        return context.getString(resId);
    }

    public void setOnItemRemovedListener(OnItemRemovedListener itemRemovedListener) {
        this.itemRemovedListener = itemRemovedListener;
    }

    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = DialogUtils.showProgressDialog(context, R.string.please_wait_dotted);
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyDataSetChanged();

        if (itemRemovedListener != null) {
            itemRemovedListener.onItemRemoved(position);
        }
    }
}
