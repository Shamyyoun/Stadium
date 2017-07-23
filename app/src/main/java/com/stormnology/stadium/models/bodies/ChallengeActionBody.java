package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.User;

/**
 * Created by Shamyyoun on 7/17/17.
 */

public class ChallengeActionBody {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("captain")
    @Expose
    private CaptainBody captain;
    @SerializedName("challenge")
    @Expose
    private Challenge challenge;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CaptainBody getCaptain() {
        return captain;
    }

    public void setCaptain(CaptainBody captain) {
        this.captain = captain;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
