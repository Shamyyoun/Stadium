package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.TeamsFilter;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class SearchController {

    public List<Team> searchTeams(List<Team> teams, TeamsFilter filter) {
        List<Team> results = new ArrayList<>();

        // check the teams list
        if (teams == null) {
            return results;
        }

        // check filter
        if (filter == null) {
            return teams;
        }

        // loop the teams to search
        for (Team team : teams) {
            // check name
            boolean nameMatches = true;
            if (!Utils.isNullOrEmpty(filter.getName())) {
                if (team.getName() != null) {
                    nameMatches = team.getName().toLowerCase().contains(filter.getName().toLowerCase());
                } else {
                    nameMatches = false;
                }
            }

            // check final result
            if (nameMatches) {
                results.add(team);
            }
        }

        return results;
    }
}
