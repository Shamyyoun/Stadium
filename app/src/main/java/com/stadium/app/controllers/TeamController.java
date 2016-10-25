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
}
