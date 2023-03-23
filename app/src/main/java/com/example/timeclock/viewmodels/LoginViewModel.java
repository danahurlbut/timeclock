package com.example.timeclock.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeclock.models.User;
import com.example.timeclock.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository; //FUTURE ENHANCEMENT replace with dependency injection

    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<User> onClick(String userNameInput, String passwordInput) {
        return userRepository.loginUser(userNameInput, passwordInput);
        //possibilities
        //1. user does not exist
        //2. incorrect password
        //3. login non-admin
        //4. login admin
    }
}
