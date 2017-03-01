package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;

/**
 * Created by Shamyyoun on 10/31/16.
 */

public class CaptainBody {
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
