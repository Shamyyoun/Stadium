
package com.stadium.app.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCity {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private Object name;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public Object getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(Object name) {
        this.name = name;
    }

}
