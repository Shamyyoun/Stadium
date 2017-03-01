package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;

public class AddMemberToTeamBody {

    @SerializedName("team")
    @Expose
    private Team team;
    @SerializedName("player")
    @Expose
    private User player;

    /**
     * @return The team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * @param team The team
     */
    public void setTeam(Team team) {
        this.team = team;
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