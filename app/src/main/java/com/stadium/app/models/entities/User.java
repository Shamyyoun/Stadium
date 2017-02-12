
package com.stadium.app.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stadium.app.models.Checkable;
import com.stadium.app.models.responses.ServerResponse;

import java.io.Serializable;

public class User extends ServerResponse implements Cloneable, Serializable, Checkable {
    public static final int TYPE_PLAYER = 1;

    @SerializedName("AdminStadium")
    @Expose
    private Stadium adminStadium;
    @SerializedName("Id")
    @Expose
    private int id;
    @SerializedName("Positon")
    @Expose
    private String position;
    @SerializedName("Rate")
    @Expose
    private double rate;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("Birthdate")
    @Expose
    private String dateOfBirth;
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
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("validationNumber")
    @Expose
    private String validationNumber;
    @SerializedName("validationStatus")
    @Expose
    private boolean validationStatus;

    private boolean checked;

    /**
     * @return The adminStadium
     */
    public Stadium getAdminStadium() {
        return adminStadium;
    }

    /**
     * @param adminStadium The AdminStadium
     */
    public void setAdminStadium(Stadium adminStadium) {
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
    public double getRate() {
        return rate;
    }

    /**
     * @param rate The Rate
     */
    public void setRate(double rate) {
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
     * @return The dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth The dateOfBirth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
     * @return The validationNumber
     */
    public String getValidationNumber() {
        return validationNumber;
    }

    /**
     * @param validationNumber The validationNumber
     */
    public void setValidationNumber(String validationNumber) {
        this.validationNumber = validationNumber;
    }

    /**
     * @return The validationStatus
     */
    public boolean isValidationStatus() {
        return validationStatus;
    }

    /**
     * @param validationStatus The validationStatus
     */
    public void setValidationStatus(boolean validationStatus) {
        this.validationStatus = validationStatus;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }
}
