package com.stadium.app.controllers;

import com.stadium.app.R;
import com.stadium.app.models.entities.PlayerRole;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;

import java.util.List;

/**
 * Created by Shamyyoun on 9/4/16.
 */
public class TeamController {
    public int getItemPosition(List<Team> teams, int itemId) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId() == itemId) {
                return i;
            }
        }

        return -1;
    }

    public boolean isCaptain(Team team, int playerId) {
        if (team.getCaptain() != null && team.getCaptain().getId() == playerId) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAssistant(Team team, int playerId) {
        if (team.getAsstent() != null && team.getAsstent().getId() == playerId) {
            return true;
        } else {
            return false;
        }
    }

    public PlayerRole getPlayerRole(Team team, int playerId) {
        // check to set the role
        if (isCaptain(team, playerId)) {
            return new PlayerRole("C", R.drawable.orange_circle);
        } else if (isAssistant(team, playerId)) {
            return new PlayerRole("A", R.drawable.green_circle);
        } else {
            return null;
        }
    }

    public boolean isTeamPlayer(List<User> teamPlayers, int playerId) {
        for (User player : teamPlayers) {
            if (playerId == player.getId()) {
                return true;
            }
        }

        return false;
    }

    public boolean isSameCaptain(Team team1, Team team2) {
        boolean sameCaptain;
        if (team1.getCaptain() == null && team2.getCaptain() == null) {
            sameCaptain = true;
        } else if (team1.getCaptain() != null && team2.getCaptain() != null) {
            if (team1.getCaptain().getId() == team2.getCaptain().getId()) {
                sameCaptain = true;
            } else {
                sameCaptain = false;
            }
        } else {
            sameCaptain = false;
        }

        return sameCaptain;
    }

    public boolean isSameAssistant(Team team1, Team team2) {
        boolean sameAssistant;
        if (team1.getAsstent() == null && team2.getAsstent() == null) {
            sameAssistant = true;
        } else if (team1.getAsstent() != null && team2.getAsstent() != null) {
            if (team1.getAsstent().getId() == team2.getAsstent().getId()) {
                sameAssistant = true;
            } else {
                sameAssistant = false;
            }
        } else {
            sameAssistant = false;
        }

        return sameAssistant;
    }
}
