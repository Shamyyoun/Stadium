package com.stormnology.stadium.models.bodies;

/**
 * Created by Shamyyoun on 10/23/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.User;

public class ConfirmPresentBody {
    @SerializedName("player")
    @Expose
    private User player;
    @SerializedName("res")
    @Expose
    private Reservation res;
    @SerializedName("Type")
    @Expose
    private int type;

    /**
     * @return The player
     */
    public User getPlayer() {
        return player;
    }

    /**
     * @param player The player
     */
    public void setPlayer(User player) {
        this.player = player;
    }

    /**
     * @return The res
     */
    public Reservation getRes() {
        return res;
    }

    /**
     * @param res The res
     */
    public void setRes(Reservation res) {
        this.res = res;
    }

    /**
     * @return The type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type The Type
     */
    public void setType(int type) {
        this.type = type;
    }
}
