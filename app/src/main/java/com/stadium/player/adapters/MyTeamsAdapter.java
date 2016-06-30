package com.stadium.player.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.stadium.player.R;
import com.stadium.player.models.MyTeamsModel;

import java.util.ArrayList;

/**
 * Created by karam on 6/30/16.
 */
public class MyTeamsAdapter extends RecyclerView.Adapter<MyTeamsAdapter.MyTeamsViewHolder> {

    private ArrayList<MyTeamsModel> myTeams;
    private Context context;

    public MyTeamsAdapter(Context context, ArrayList<MyTeamsModel> myTeams) {
        this.context = context;
        this.myTeams = myTeams;
    }


    @Override
    public MyTeamsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_team_list, parent, false);

        MyTeamsViewHolder holder = new MyTeamsViewHolder(itemLayoutView);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyTeamsViewHolder holder, int position) {
        final MyTeamsModel thisMyTeam = myTeams.get(position);

        holder.teamName.setText(thisMyTeam.getTeamName());

        if (thisMyTeam.getTeamImage() != null){
            Picasso.with(context).load(thisMyTeam.getTeamImage()).noFade().into(holder.teamImage);
        }

    }

    @Override
    public int getItemCount() {
        return myTeams.size();
    }


    public class MyTeamsViewHolder extends RecyclerView.ViewHolder {

        private TextView teamName;
        private RoundedImageView teamImage;
        private RoundedImageView teamRule;


        public MyTeamsViewHolder(final View itemView) {
            super(itemView);

            teamName = (TextView) itemView.findViewById(R.id.myTeam_name);
            teamImage = (RoundedImageView) itemView.findViewById(R.id.myTeam_image);
            teamRule = (RoundedImageView) itemView.findViewById(R.id.teamRule_image);


        }
    }
}
