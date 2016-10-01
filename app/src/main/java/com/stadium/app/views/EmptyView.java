package com.stadium.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.stadium.app.R;

/**
 * Created by Shamyyoun on 3/9/2016.
 */
public class EmptyView extends FrameLayout {
    private Context context;
    private View rootView;
    private TextView tvEmpty;

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.view_empty, this);
        tvEmpty = (TextView) rootView.findViewById(R.id.tv_empty);
    }

    public void setEmpty(int msgResId) {
        if (tvEmpty != null) {
            tvEmpty.setText(msgResId);
        }
    }

    public void setEmpty(String msg) {
        if (tvEmpty != null) {
            tvEmpty.setText(msg);
        }
    }
}
