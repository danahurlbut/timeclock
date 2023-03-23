package com.example.timeclock.repositories;

import com.example.timeclock.models.User;
import com.example.timeclock.models.UserList;
import com.example.timeclock.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface UserInterface {

    @GET("/api/users/{userId}")
    Call<UserResponse> loginUser(
            @Path("userId") String userId
    );

    @PATCH("/api/users")
    Call<UserResponse> updateUser(
            @Body User user
    );

    @GET("/api/users")
    Call<UserList> getUserList();
}
