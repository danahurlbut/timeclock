package com.example.timeclock.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeclock.models.AdminUser;
import com.example.timeclock.models.UserList;
import com.example.timeclock.repositories.UserRepository;

public class AdminViewModel extends ViewModel {
    private AdminUser user;
    private MutableLiveData<UserList> mutableLiveDataUserList;
    private final UserRepository userRepository; //FUTURE ENHANCEMENT replace with dependency injection

    public AdminViewModel() {
        userRepository = new UserRepository();
    }

    public void setAdminUser(AdminUser user) {
        this.user = user;
    }

    public AdminUser getAdminUser() {
        return user;
    }

    public LiveData<UserList> getUserList() {
        if (mutableLiveDataUserList == null || mutableLiveDataUserList.getValue() == null) {
            mutableLiveDataUserList = userRepository.getUserList();
        }
        return mutableLiveDataUserList;
    }
}
