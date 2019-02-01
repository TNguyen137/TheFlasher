package com.android.theflasherapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.maps.model.LatLng;

public class UserInfo {

    @JsonProperty("UserId")
    private int userId;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("Num")
    private String num;
    @JsonProperty("Email")
    private String email;

    public UserInfo() {

    }

    public UserInfo(int userId, String location, String firstName, String lastName, String num, String email){
        this.userId = userId;
        this.location = location;
        this.firstName = firstName;
        this.lastName = lastName;
        this.num = num;
        this.email = email;
    }

    public int getId() {
        return this.userId;
    }

    public String getLocation() {
        return this.location;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() { return this.lastName; }

    public String getNum() {
        return this.num;
    }

    public String getEmail() {
        return this.email;
    }
}
