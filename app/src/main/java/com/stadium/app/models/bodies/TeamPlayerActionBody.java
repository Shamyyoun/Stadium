package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Team;
import com.stadium.app.models.entities.User;

public class TeamPlayerActionBody {

    @SerializedName("captain")
    @Expose
    private Captain captain;
    @SerializedName("playerId")
    @Expose
    private int playerId;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * @return The captain
     */
    public Captain getCaptain() {
        return captain;
    }

    /**
     * @param captain The captain
     */
    public void setCaptain(Captain captain) {
        this.captain = captain;
    }

    /**
     * @return The playerId
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId The playerId
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    // -------------------- Captain Class --------------------
    public static class Captain {

        @SerializedName("userinfo")
        @Expose
        private User userinfo;
        @SerializedName("HisTeam")
        @Expose
        private Team hisTeam;

        /**
         * @return The userinfo
         */
        public User getUserinfo() {
            return userinfo;
        }

        /**
         * @param userinfo The userinfo
         */
        public void setUserinfo(User userinfo) {
            this.userinfo = userinfo;
        }

        /**
         * @return The hisTeam
         */
        public Team getHisTeam() {
            return hisTeam;
        }

        /**
         * @param hisTeam The HisTeam
         */
        public void setHisTeam(Team hisTeam) {
            this.hisTeam = hisTeam;
        }

    }
}