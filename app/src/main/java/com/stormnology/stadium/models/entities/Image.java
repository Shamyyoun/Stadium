package com.stormnology.stadium.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {

    @SerializedName("contentBase64")
    @Expose
    private String contentBase64;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * @return The contentBase64
     */
    public String getContentBase64() {
        return contentBase64;
    }

    /**
     * @param contentBase64 The contentBase64
     */
    public void setContentBase64(String contentBase64) {
        this.contentBase64 = contentBase64;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

}