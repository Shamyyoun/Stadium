
package com.stormnology.stadium.models.entities;

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
    @SerializedName("confirmStatusId")
    @Expose
    private int confirmStatusId;
    @SerializedName("confirmStatus")
    @Expose
    private String confirmStatus;
    @SerializedName("challengeId")
    @Expose
    private int challengeId;
    @SerializedName("challengeType")
    @Expose
    private int challengeType;
    @SerializedName("guestCaptain")
    @Expose
    private int guestCaptain;
    @SerializedName("guestAssistant")
    @Expose
    private int guestAssistant;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public int getPicType() {
        return picType;
    }

    public void setPicType(int picType) {
        this.picType = picType;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleType() {
        return titleType;
    }

    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }

    public int getConfirmStatusId() {
        return confirmStatusId;
    }

    public void setConfirmStatusId(int confirmStatusId) {
        this.confirmStatusId = confirmStatusId;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(int challengeType) {
        this.challengeType = challengeType;
    }

    public int getGuestCaptain() {
        return guestCaptain;
    }

    public void setGuestCaptain(int guestCaptain) {
        this.guestCaptain = guestCaptain;
    }

    public int getGuestAssistant() {
        return guestAssistant;
    }

    public void setGuestAssistant(int guestAssistant) {
        this.guestAssistant = guestAssistant;
    }
}
