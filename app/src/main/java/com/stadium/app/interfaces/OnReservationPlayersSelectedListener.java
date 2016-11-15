package com.stadium.app.interfaces;

import com.stadium.app.models.entities.User;

import java.util.List;

/**
 * Created by Shamyyoun on 5/7/16.
 */
public interface OnReservationPlayersSelectedListener {
    public void onPlayersSelected(List<User> selectedPlayers, int playersCount);
}
