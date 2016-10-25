package com.stadium.app.controllers;

import com.stadium.app.models.entities.Team;

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

    public static boolean isCaptain(Team team, int playerId) {
        if (team.getCaptain() != null && team.getCaptain().getId() == playerId) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAssistant(Team team, int playerId) {
        if (team.getCaptain() != null && team.getAsstent().getId() == playerId) {
            return true;
        } else {
            return false;
        }
    }
}
