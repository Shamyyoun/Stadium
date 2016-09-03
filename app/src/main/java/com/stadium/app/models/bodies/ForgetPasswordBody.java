
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPasswordBody extends ParentBody {

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

    /**
     * User class
     */
    public static class User {

        @SerializedName("phone")
        @Expose
        private String phone;

        /**
         * @return The phone
         */
        public String getPhone() {
            return phone;
        }

        /**
         * @param phone The phone
         */
        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
