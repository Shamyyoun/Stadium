
package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stormnology.stadium.models.entities.User;

public class ForgetPasswordBody {

    @SerializedName("restType")
    @Expose
    private int restType;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * @return The restType
     */
    public int getRestType() {
        return restType;
    }

    /**
     * @param restType The restType
     */
    public void setRestType(int restType) {
        this.restType = restType;
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
