package com.stormnology.stadium.interfaces;

import com.stormnology.stadium.models.entities.User;

import java.util.List;

/**
 * Created by Shamyyoun on 5/7/16.
 */
public interface OnReservationPlayersSelectedListener {
    public void onPlayersSelected(List<User> selectedPlayers, int playersCount);
}
