package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;

public class EditTeamBody {

    @SerializedName("HisTeam")
    @Expose
    private Team hisTeam;
    @SerializedName("userinfo")
    @Expose
    private User userinfo;

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

}
