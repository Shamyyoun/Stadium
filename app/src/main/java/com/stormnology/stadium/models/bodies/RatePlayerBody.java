package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.User;

public class RatePlayerBody {

    @SerializedName("Rate")
    @Expose
    private double rate;
    @SerializedName("playerRated")
    @Expose
    private User playerRated;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * @return The rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate The Rate
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return The playerRated
     */
    public User getPlayerRated() {
        return playerRated;
    }

    /**
     * @param playerRated The playerRated
     */
    public void setPlayerRated(User playerRated) {
        this.playerRated = playerRated;
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
}
