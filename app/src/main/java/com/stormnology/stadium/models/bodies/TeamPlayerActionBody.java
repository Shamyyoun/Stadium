package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.User;

public class TeamPlayerActionBody {

    @SerializedName("captain")
    @Expose
    private CaptainBody captain;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("player")
    @Expose
    private User player;

    /**
     * @return The captain
     */
    public CaptainBody getCaptain() {
        return captain;
    }

    /**
     * @param captain The captain
     */
    public void setCaptain(CaptainBody captain) {
        this.captain = captain;
    }

    /**
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

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
}