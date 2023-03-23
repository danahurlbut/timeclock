package com.example.timeclock.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("data")
    private User user;

    public UserResponse() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
