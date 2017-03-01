package com.stormnology.stadium.controllers;

import com.stormnology.stadium.models.entities.PlayersFilter;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/2/16.
 */

public class SearchController {

    public List<User> searchPlayers(List<User> players, PlayersFilter filter) {
        List<User> results = new ArrayList<>();

        // check the players list
        if (players == null) {
            return results;
        }

        // check filter
        if (filter == null) {
            return players;
        }

        // loop the players to search
        for (User player : players) {
            // check city
            boolean cityMatches = true;
            if (filter.getCity() != null) {
                if (player.getCity() != null) {
                    cityMatches = player.getCity().getId() == filter.getCity().getId();
                } else {
                    cityMatches = false;
                }
            }

            // check name
            boolean nameMatches = true;
            if (!Utils.isNullOrEmpty(filter.getName())) {
                if (player.getName() != null) {
                    nameMatches = player.getName().toLowerCase().contains(filter.getName().toLowerCase());
                } else {
                    nameMatches = false;
                }
            }

            // check position
            boolean positionMatches = true;
            if (!Utils.isNullOrEmpty(filter.getPosition())) {
                if (player.getPosition() != null) {
                    positionMatches = player.getPosition().toLowerCase().contains(filter.getPosition().toLowerCase());
                } else {
                    positionMatches = false;
                }
            }

            // check final result
            if (cityMatches && nameMatches && positionMatches) {
                results.add(player);
            }
        }

        return results;
    }
}
