package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.Checkable;

import java.io.Serializable;


public class Stadium implements Serializable, Checkable {

    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("StadiumImage")
    @Expose
    private Object stadiumImage;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fieldsCount")
    @Expose
    private int fieldsCount;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phonenumber")
    @Expose
    private String phoneNumber;
    @SerializedName("pio")
    @Expose
    private String bio;
    @SerializedName("rate")
    @Expose
    private float rate;
    @SerializedName("rateCounter")
    @Expose
    private int rateCounter;
    @SerializedName("staduimCity")
    @Expose
    private City stadiumCity;
    private boolean checked;

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
     * @return The stadiumImage
     */
    public Object getStadiumImage() {
        return stadiumImage;
    }

    /**
     * @param stadiumImage The StadiumImage
     */
    public void setStadiumImage(Object stadiumImage) {
        this.stadiumImage = stadiumImage;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The fieldsCount
     */
    public int getFieldsCount() {
        return fieldsCount;
    }

    /**
     * @param fieldsCount The fieldsCount
     */
    public void setFieldsCount(int fieldsCount) {
        this.fieldsCount = fieldsCount;
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
     * @return The latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    /**
     * @return The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * @param bio The bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * @return The rate
     */
    public float getRate() {
        return rate;
    }

    /**
     * @param rate The rate
     */
    public void setRate(float rate) {
        this.rate = rate;
    }

    /**
     * @return The rateCounter
     */
    public int getRateCounter() {
        return rateCounter;
    }

    /**
     * @param rateCounter The rateCounter
     */
    public void setRateCounter(int rateCounter) {
        this.rateCounter = rateCounter;
    }

    /**
     * @return The stadiumCity
     */
    public City getStadiumCity() {
        return stadiumCity;
    }

    /**
     * @param stadiumCity The stadiumCity
     */
    public void setStadiumCity(City stadiumCity) {
        this.stadiumCity = stadiumCity;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public String toString() {
        return name;
    }
}
