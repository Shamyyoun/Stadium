package com.stormnology.stadium.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 11/24/16.
 */

public class CheckListOfContactsBody {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("contacts")
    @Expose
    private List<String> contacts = new ArrayList<>();

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The contacts
     */
    public List<String> getContacts() {
        return contacts;
    }

    /**
     * @param contacts The contacts
     */
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }
}
