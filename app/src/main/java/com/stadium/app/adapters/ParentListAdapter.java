package com.stadium.app.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

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

    public ParentListAdapter(Context context, int layoutId, List<Item> data) {
        super(context, layoutId, data);
        this.context = context;
        this.layoutId = layoutId;
        this.data = data;
    }

    public ParentListAdapter(Context context, int layoutId, Item[] data) {
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
}
