
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.User;

public class AdminBody {

    @SerializedName("HisStadium")
    @Expose
    private Stadium hisStadium;
    @SerializedName("userinfo")
    @Expose
    private User userinfo;

    /**
     * @return The hisStadium
     */
    public Stadium getHisStadium() {
        return hisStadium;
    }

    /**
     * @param hisStadium The HisStadium
     */
    public void setHisStadium(Stadium hisStadium) {
        this.hisStadium = hisStadium;
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
