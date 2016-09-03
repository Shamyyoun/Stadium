
package com.stadium.app.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckEmailBody extends ParentBody {

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
