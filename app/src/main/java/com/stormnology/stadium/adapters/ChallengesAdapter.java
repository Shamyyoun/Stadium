package com.stormnology.stadium.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.R;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.enums.ChallengesType;

import java.util.List;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ChallengesAdapter extends ParentRecyclerAdapter<Challenge> {
    private ChallengesType challengesType;

    public ChallengesAdapter(Context context, List<Challenge> data, int layoutId) {
        super(context, data, layoutId);
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
    }

    class ViewHolder extends ParentRecyclerViewHolder {
        private TextView tvCreationDate;
        private TextView tvHostTeamName;
        private ImageView ivHostTeamImage;
        private TextView tvHostTeamComment;
        private TextView tvGuestTeamName;
        private ImageView ivGuestTeamImage;
        private TextView tvGuestTeamComment;
        private TextView tvHostTeamScore;
        private TextView tvGuestTeamScore;
        private TextView tvPlace;
        private TextView tvDateTime;
        private View layoutButtons;
        private Button btnAccept;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvCreationDate = (TextView) findViewById(R.id.tv_creation_date);
            tvHostTeamName = (TextView) findViewById(R.id.tv_host_team_name);
            ivHostTeamImage = (ImageView) findViewById(R.id.iv_host_team_image);
            tvHostTeamComment = (TextView) findViewById(R.id.tv_host_team_comment);
            tvGuestTeamName = (TextView) findViewById(R.id.tv_guest_team_name);
            ivGuestTeamImage = (ImageView) findViewById(R.id.iv_guest_team_image);
            tvGuestTeamComment = (TextView) findViewById(R.id.tv_guest_team_comment);
            tvHostTeamScore = (TextView) findViewById(R.id.tv_host_team_score);
            tvGuestTeamScore = (TextView) findViewById(R.id.tv_guest_team_score);
            tvPlace = (TextView) findViewById(R.id.tv_place);
            tvDateTime = (TextView) findViewById(R.id.tv_date_time);
            layoutButtons = findViewById(R.id.layout_buttons);
            btnAccept = (Button) findViewById(R.id.btn_accept);
        }
    }

    public void setChallengesType(ChallengesType challengesType) {
        this.challengesType = challengesType;
    }
}
