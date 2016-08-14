
package com.stadium.app.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("AdminStadium")
    @Expose
    private Object adminStadium;
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Positon")
    @Expose
    private Object positon;
    @SerializedName("Rate")
    @Expose
    private int rate;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("newPassword")
    @Expose
    private Object newPassword;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("typeID")
    @Expose
    private int typeID;
    @SerializedName("userCity")
    @Expose
    private UserCity userCity;
    @SerializedName("userImage")
    @Expose
    private Object userImage;

    /**
     * @return The adminStadium
     */
    public Object getAdminStadium() {
        return adminStadium;
    }

    /**
     * @param adminStadium The AdminStadium
     */
    public void setAdminStadium(Object adminStadium) {
        this.adminStadium = adminStadium;
    }

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
     * @return The positon
     */
    public Object getPositon() {
        return positon;
    }

    /**
     * @param positon The Positon
     */
    public void setPositon(Object positon) {
        this.positon = positon;
    }

    /**
     * @return The rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * @param rate The Rate
     */
    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * @return The age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age The age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return The email
     */
    public Object getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(Object email) {
        this.email = email;
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
     * @return The newPassword
     */
    public Object getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword The newPassword
     */
    public void setNewPassword(Object newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

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

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return The typeID
     */
    public int getTypeID() {
        return typeID;
    }

    /**
     * @param typeID The typeID
     */
    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    /**
     * @return The userCity
     */
    public UserCity getUserCity() {
        return userCity;
    }

    /**
     * @param userCity The userCity
     */
    public void setUserCity(UserCity userCity) {
        this.userCity = userCity;
    }

    /**
     * @return The userImage
     */
    public Object getUserImage() {
        return userImage;
    }

    /**
     * @param userImage The userImage
     */
    public void setUserImage(Object userImage) {
        this.userImage = userImage;
    }

}
