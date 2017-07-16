package com.stormnology.stadium.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stormnology.stadium.R;
import com.stormnology.stadium.controllers.ActiveUserController;
import com.stormnology.stadium.controllers.ChallengeController;
import com.stormnology.stadium.controllers.ReservationController;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.enums.ChallengesType;
import com.stormnology.stadium.utils.DialogUtils;
import com.stormnology.stadium.utils.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

/**
 * Created by Shamyyoun on 19/2/16.
 */
public class ChallengesAdapter extends ParentRecyclerAdapter<Challenge> {
    private ChallengesType challengesType;

    private ActiveUserController activeUserController;
    private ChallengeController challengeController;
    private ReservationController reservationController;
    private PrettyTime prettyTime;

    public ChallengesAdapter(Context context, List<Challenge> data, int layoutId) {
        super(context, data, layoutId);

        // obtain main objects
        activeUserController = new ActiveUserController(context);
        challengeController = new ChallengeController();
        reservationController = new ReservationController();
        prettyTime = new PrettyTime(new Locale("ar"));
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
        ViewHolder holder = (ViewHolder) viewHolder;

        // get item
        Challenge item = data.get(position);

        // set date
        String creationDate = challengeController.getCreationDate(item, prettyTime);
        if (creationDate != null) {
            holder.tvCreationDate.setText(creationDate);
            holder.tvCreationDate.setVisibility(View.VISIBLE);
        } else {
            holder.tvCreationDate.setVisibility(View.GONE);
        }

        // set host team info
        Team hostTeam = item.getHostTeam();
        holder.tvHostTeamName.setText(challengeController.getHostTeamName(item));
        Utils.loadImage(context, hostTeam.getImageLink(), R.drawable.default_image, holder.ivHostTeamImage);

        // set its comment
        if (!Utils.isNullOrEmpty(item.getHostComment())) {
            holder.tvHostTeamComment.setText(item.getHostComment());
            holder.tvHostTeamComment.setVisibility(View.VISIBLE);
        } else {
            holder.tvHostTeamComment.setVisibility(View.GONE);
        }

        // set guest team info
        Team guestTeam = item.getGuestTeam();
        holder.tvGuestTeamName.setText(challengeController.getGuestTeamName(context, item));
        Utils.loadImage(context, guestTeam.getImageLink(), R.drawable.default_image, holder.ivGuestTeamImage);

        // set its comment
        if (!Utils.isNullOrEmpty(item.getGuestComment())) {
            holder.tvGuestTeamComment.setText(item.getGuestComment());
            holder.tvGuestTeamComment.setVisibility(View.VISIBLE);
        } else {
            holder.tvGuestTeamComment.setVisibility(View.GONE);
        }

        // set place info
        String placeInfo = challengeController.getPlaceInfo(context, item);
        if (!Utils.isNullOrEmpty(placeInfo)) {
            holder.tvPlace.setText(placeInfo);
            holder.tvPlace.setVisibility(View.VISIBLE);
        } else {
            holder.tvPlace.setVisibility(View.GONE);
        }

        // set date info
        String dateInfo = challengeController.getDateInfo(item);
        if (!Utils.isNullOrEmpty(dateInfo)) {
            holder.tvDateTime.setText(dateInfo);
            holder.tvDateTime.setVisibility(View.VISIBLE);
        } else {
            holder.tvDateTime.setVisibility(View.GONE);
        }

        // check to show / hide the top divider
        if (Utils.isNullOrEmpty(placeInfo) && Utils.isNullOrEmpty(dateInfo)) {
            holder.viewTopDivider.setVisibility(View.GONE);
        } else {
            holder.viewTopDivider.setVisibility(View.VISIBLE);
        }

        // check if current user can accept the challenge
        User user = activeUserController.getUser();
        if (challengeController.playerCanAcceptChallenge(user.getId(), item)) {
            // show buttons layout
            holder.layoutButtons.setVisibility(View.VISIBLE);
        } else {
            // hide buttons layout
            holder.layoutButtons.setVisibility(View.GONE);
        }

        // add listeners
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAcceptConfirmDialog(position);
            }
        });
    }

    private void showAcceptConfirmDialog(final int position) {
        DialogUtils.showConfirmDialog(context, R.string.accept_challenge_q, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accept(position);
            }
        }, null);
    }

    private void accept(int position) {
        // TODO implement this method
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
        private View viewTopDivider;
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
            viewTopDivider = findViewById(R.id.view_top_divider);
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
