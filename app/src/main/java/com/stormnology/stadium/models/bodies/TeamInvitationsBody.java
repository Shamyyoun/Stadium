package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.User;

public class TeamInvitationsBody {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("captin")
    @Expose
    private User captain;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The captain
     */
    public User getCaptain() {
        return captain;
    }

    /**
     * @param captain The captain
     */
    public void setCaptain(User captain) {
        this.captain = captain;
    }
}