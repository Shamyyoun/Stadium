
package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {
    public static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("eventType")
    @Expose
    private int eventType;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("picId")
    @Expose
    private int picId;
    @SerializedName("picType")
    @Expose
    private int picType;
    @SerializedName("resId")
    @Expose
    private int resId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("titleId")
    @Expose
    private int titleId;
    @SerializedName("titleType")
    @Expose
    private int titleType;

    private int confirmStatus; // confirmPresent status - used in the UI only

    /**
     * @return The date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The event type
     */
    public int getEventType() {
        return eventType;
    }

    /**
     * @param eventType The event type
     */
    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    /**
     * @return The imageLink
     */
    public String getImageLink() {
        return imageLink;
    }

    /**
     * @param imageLink The imageLink
     */
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    /**
     * @return The picId
     */
    public int getPicId() {
        return picId;
    }

    /**
     * @param picId The picId
     */
    public void setPicId(int picId) {
        this.picId = picId;
    }

    /**
     * @return The picType
     */
    public int getPicType() {
        return picType;
    }

    /**
     * @param picType The picType
     */
    public void setPicType(int picType) {
        this.picType = picType;
    }

    /**
     * @return The resId
     */
    public int getResId() {
        return resId;
    }

    /**
     * @param resId The resId
     */
    public void setResId(int resId) {
        this.resId = resId;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The titleId
     */
    public int getTitleId() {
        return titleId;
    }

    /**
     * @param titleId The titleId
     */
    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    /**
     * @return The titleType
     */
    public int getTitleType() {
        return titleType;
    }

    /**
     * @param titleType The titleType
     */
    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }

    public void setConfirmStatus(int confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public int getConfirmStatus() {
        return confirmStatus;
    }
}
