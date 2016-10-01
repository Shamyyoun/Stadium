package com.stadium.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stadium.app.R;

/**
 * Created by Shamyyoun on 3/9/2016.
 */
public class ErrorView extends FrameLayout {
    private Context context;
    private View rootView;
    private TextView tvError;
    private ImageButton ibRefresh;

    public ErrorView(Context context) {
        super(context);
        init(context);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.view_error, this);
        tvError = (TextView) rootView.findViewById(R.id.tv_error);
        ibRefresh = (ImageButton) rootView.findViewById(R.id.ib_refresh);
    }

    public void setRefreshListener(OnClickListener clickListener) {
        if (ibRefresh != null) {
            ibRefresh.setOnClickListener(clickListener);
        }
    }

    public void setError(int msgResId) {
        if (tvError != null) {
            tvError.setText(msgResId);
        }
    }

    public void setError(String msg) {
        if (tvError != null) {
            tvError.setText(msg);
        }
    }
}
