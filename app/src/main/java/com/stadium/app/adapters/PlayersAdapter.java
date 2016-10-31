package com.stadium.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.stadium.app.Const;
import com.stadium.app.R;
import com.stadium.app.activities.PlayerInfoActivity;
import com.stadium.app.controllers.UserController;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.Utils;

import java.util.List;

/**
 * Created by karam on 7/17/16.
 */
public class PlayersAdapter extends ParentRecyclerAdapter<User> {
    private UserController userController;
    private boolean isSimpleView;

    public PlayersAdapter(Context context, List<User> data, int layoutId) {
        super(context, data, layoutId);

        // set simple view flag
        isSimpleView = (layoutId == R.layout.item_player_simple);
    }

    @Override
    public ParentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.setOnItemClickListener(itemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ParentRecyclerViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;

        // load the image as it is shared among all views
        final User item = data.get(position);
        Utils.loadImage(context, item.getImageLink(), R.drawable.default_image, holder.ivImage);

        // check if simple view
        if (isSimpleView) {
            // set simple name
            holder.tvName.setText(item.getName());
        } else {
            // set basic data
            userController = new UserController(item);
            holder.tvName.setText(userController.getNamePosition());
            holder.rbRating.setRating((float) item.getRate());

            // set address
            String address = userController.getCityName();
            if (address != null) {
                holder.tvAddress.setText(address);
                holder.tvAddress.setVisibility(View.VISIBLE);
            } else {
                holder.tvAddress.setVisibility(View.GONE);
            }

            // create global click listener
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.layout_content:
                            openPlayerInfo(position);
                            break;

                        case R.id.ib_add:
                            // TODO
                            break;
                    }
                }
            };

            // add listeners
            holder.layoutContent.setOnClickListener(clickListener);
            holder.ibAdd.setOnClickListener(clickListener);
        }
    }

    private void openPlayerInfo(int position) {
        User user = data.get(position);
        Intent intent = new Intent(context, PlayerInfoActivity.class);
        intent.putExtra(Const.KEY_ID, user.getId());
        context.startActivity(intent);
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private View layoutContent;
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvAddress;
        private RatingBar rbRating;
        private ImageButton ibAdd;

        public ViewHolder(final View itemView) {
            super(itemView);

            layoutContent = findViewById(R.id.layout_content);
            ivImage = (ImageView) findViewById(R.id.iv_image);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvAddress = (TextView) findViewById(R.id.tv_address);
            rbRating = (RatingBar) findViewById(R.id.rb_rating);
            ibAdd = (ImageButton) findViewById(R.id.ib_add);
        }
    }
}
