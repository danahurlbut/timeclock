package com.example.timeclock.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserList {
    @SerializedName("data")
    private List<User> users;

    public UserList() {

    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
