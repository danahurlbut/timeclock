package com.example.timeclock.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.timeclock.models.User;
import com.example.timeclock.models.UserList;
import com.example.timeclock.models.UserResponse;
import com.example.timeclock.services.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserInterface userInterface;
    private final MutableLiveData<User> userMutableLiveData;
    private final MutableLiveData<UserList> userListMutableLiveData;

    public UserRepository() {
        userInterface = APIClient.getAPIClient().create(UserInterface.class);
        userMutableLiveData = new MutableLiveData<>();
        userListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> loginUser(String userName, String password) {
//        TODO when using the real API, we will of course pass in password as well.
//         For now am mocking that the correct PW is "wordpass" for all users
        if ("wordpass".equals(password)) {
            userInterface.loginUser(userName).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null)
                        userMutableLiveData.setValue(response.body().getUser());
                }

                @Override
                public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                    userMutableLiveData.setValue(null); //future enhancement -- handle different error types differently
                }
            });
        } else
            userMutableLiveData.setValue(null);

        return userMutableLiveData;
    }

    //to be used when admin updates a user, or when user updates their shift
    public MutableLiveData<User> updateUser(User user) {
        userInterface.updateUser(user).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    userMutableLiveData.setValue(response.body().getUser());
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                userMutableLiveData.setValue(null); //future enhancement -- handle different error types differently
            }
        });

        return userMutableLiveData;
    }

    public MutableLiveData<UserList> getUserList() {
        userInterface.getUserList().enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(@NonNull Call<UserList> call, @NonNull Response<UserList> response) {
                if (response.isSuccessful() && response.body() != null)
                    userListMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserList> call, @NonNull Throwable t) {
                userListMutableLiveData.setValue(null);
            }
        });
        return userListMutableLiveData;
    }
}
