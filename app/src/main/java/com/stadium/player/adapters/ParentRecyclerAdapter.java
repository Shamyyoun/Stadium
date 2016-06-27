package com.stadium.player.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.stadium.player.interfaces.OnItemClickListener;
import com.stadium.player.utils.Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shamyyoun on 5/11/16.
 */
public abstract class ParentRecyclerAdapter<Item> extends RecyclerView.Adapter<ParentViewHolder> {
    protected Context context;
    protected List<Item> data;
    protected int layoutId;
    protected OnItemClickListener itemClickListener;

    public ParentRecyclerAdapter(Context context, List<Item> data, int layoutId) {
        this.context = context;
        this.data = data;
        this.layoutId = layoutId;
    }

    public ParentRecyclerAdapter(Context context, Item[] data, int layoutId) {
        this.context = context;
        this.data = Arrays.asList(data);
        this.layoutId = layoutId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    protected void logE(String msg) {
        Utils.logE(msg);
    }

    protected String getString(int resId) {
        return context.getString(resId);
    }
}
