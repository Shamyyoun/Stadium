
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.entities.Team;


public class UnblockTeamBody {

    @SerializedName("team")
    @Expose
    private Team team;
    @SerializedName("admin")
    @Expose
    private AdminBody admin;

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
     * @return The admin
     */
    public AdminBody getAdmin() {
        return admin;
    }

    /**
     * @param admin The admin
     */
    public void setAdmin(AdminBody admin) {
        this.admin = admin;
    }

}
