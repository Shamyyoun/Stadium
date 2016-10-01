
package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.responses.ServerResponse;

public class User extends ServerResponse {
    public static final int TYPE_PLAYER = 1;

    @SerializedName("AdminStadium")
    @Expose
    private Object adminStadium;
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Positon")
    @Expose
    private String position;
    @SerializedName("Rate")
    @Expose
    private int rate;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("imageLink")
    @Expose
    private String imageLink;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
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
    private City city;
    @SerializedName("userImage")
    @Expose
    private Image userImage;

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
     * @return The position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position The position
     */
    public void setPosition(String position) {
        this.position = position;
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
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword The newPassword
     */
    public void setNewPassword(String newPassword) {
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
     * @return The city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return The userImage
     */
    public Image getUserImage() {
        return userImage;
    }

    /**
     * @param userImage The userImage
     */
    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

}
